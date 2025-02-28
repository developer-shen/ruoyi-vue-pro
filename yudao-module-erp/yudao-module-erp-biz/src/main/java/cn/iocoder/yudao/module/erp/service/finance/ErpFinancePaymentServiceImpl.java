package cn.iocoder.yudao.module.erp.service.finance;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.erp.controller.admin.finance.vo.payment.ErpFinancePaymentPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.finance.vo.payment.ErpFinancePaymentSaveReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.purchase.vo.order.ErpPurchaseOrderPageReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.ErpFinancePaymentDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.ErpFinancePaymentItemDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpPurchaseInDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpPurchaseOrderDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpPurchaseReturnDO;
import cn.iocoder.yudao.module.erp.dal.mysql.finance.ErpFinancePaymentItemMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.finance.ErpFinancePaymentMapper;
import cn.iocoder.yudao.module.erp.dal.redis.no.ErpNoRedisDAO;
import cn.iocoder.yudao.module.erp.enums.ErpAuditStatus;
import cn.iocoder.yudao.module.erp.enums.common.ErpBizTypeEnum;
import cn.iocoder.yudao.module.erp.service.financepaymentlist.ErpFinancePaymentListService;
import cn.iocoder.yudao.module.erp.service.purchase.ErpPurchaseInService;
import cn.iocoder.yudao.module.erp.service.purchase.ErpPurchaseOrderService;
import cn.iocoder.yudao.module.erp.service.purchase.ErpPurchaseReturnService;
import cn.iocoder.yudao.module.erp.service.purchase.ErpSupplierService;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.*;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

// TODO 芋艿：记录操作日志

/**
 * ERP 付款单 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ErpFinancePaymentServiceImpl implements ErpFinancePaymentService {

    @Resource
    private ErpFinancePaymentMapper financePaymentMapper;
    @Resource
    private ErpFinancePaymentItemMapper financePaymentItemMapper;

    @Resource
    private ErpNoRedisDAO noRedisDAO;

    @Resource
    private ErpSupplierService supplierService;
    @Resource
    private ErpAccountService accountService;
    @Resource
    private ErpPurchaseInService purchaseInService;
    @Resource
    private ErpPurchaseReturnService purchaseReturnService;

    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private ErpFinancePaymentListService financePaymentListService;

    @Resource
    private ErpPurchaseOrderService purchaseOrderService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createFinancePayment(ErpFinancePaymentSaveReqVO createReqVO) {
        // 1.1 校验供应商
        supplierService.validateSupplier(createReqVO.getSupplierId());
        // 1.2 校验结算账户
        if (createReqVO.getAccountId() != null) {
            accountService.validateAccount(createReqVO.getAccountId());
        }
        // 1.3 校验付款清单编号有效性
        if (createReqVO.getPaymentListId() != null) {
            financePaymentListService.validateFinancePaymentListExists(createReqVO.getPaymentListId());
        }
        // 1.4 生成付款单号，并校验唯一性
        String no = noRedisDAO.generate(ErpNoRedisDAO.FINANCE_PAYMENT_NO_PREFIX);
        if (financePaymentMapper.selectByNo(no) != null) {
            throw exception(FINANCE_PAYMENT_NO_EXISTS);
        }

        // 2.1 插入付款单
        ErpFinancePaymentDO payment = BeanUtils.toBean(createReqVO, ErpFinancePaymentDO.class, in -> in
                .setNo(no).setStatus(ErpAuditStatus.PROCESS.getStatus()));
        financePaymentMapper.insert(payment);

        return payment.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFinancePayment(ErpFinancePaymentSaveReqVO updateReqVO) {
        // 1.1 校验存在
        ErpFinancePaymentDO payment = validateFinancePaymentExists(updateReqVO.getId());
        if (ErpAuditStatus.APPROVE.getStatus().equals(payment.getStatus())) {
            throw exception(FINANCE_PAYMENT_UPDATE_FAIL_APPROVE, payment.getNo());
        }
        // 1.2 校验供应商
        supplierService.validateSupplier(updateReqVO.getSupplierId());
        // 1.3 校验结算账户
        if (updateReqVO.getAccountId() != null) {
            accountService.validateAccount(updateReqVO.getAccountId());
        }
        // 1.4 校验付款清单编号有效性
        if (updateReqVO.getPaymentListId() != null) {
            financePaymentListService.validateFinancePaymentListExists(updateReqVO.getPaymentListId());
        }

        // 2.1 更新付款单
        ErpFinancePaymentDO updateObj = BeanUtils.toBean(updateReqVO, ErpFinancePaymentDO.class);
        financePaymentMapper.updateById(updateObj);
    }

    private void calculateTotalPrice(ErpFinancePaymentDO payment, List<ErpFinancePaymentItemDO> paymentItems) {
        payment.setTotalPrice(getSumValue(paymentItems, ErpFinancePaymentItemDO::getPaymentPrice, BigDecimal::add, BigDecimal.ZERO));
        payment.setPaymentPrice(payment.getTotalPrice().subtract(payment.getDiscountPrice()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFinancePaymentStatus(Long id, Integer status) {
        boolean approve = ErpAuditStatus.APPROVE.getStatus().equals(status);
        // 1.1 校验存在
        ErpFinancePaymentDO payment = validateFinancePaymentExists(id);
        // 1.2 校验状态
        if (payment.getStatus().equals(status)) {
            throw exception(approve ? FINANCE_PAYMENT_APPROVE_FAIL : FINANCE_PAYMENT_PROCESS_FAIL);
        }

        // 2. 更新状态
        int updateCount = financePaymentMapper.updateByIdAndStatus(id, payment.getStatus(),
                new ErpFinancePaymentDO().setStatus(status));
        if (updateCount == 0) {
            throw exception(approve ? FINANCE_PAYMENT_APPROVE_FAIL : FINANCE_PAYMENT_PROCESS_FAIL);
        }
    }

    private List<ErpFinancePaymentItemDO> validateFinancePaymentItems(
            Long supplierId,
            List<ErpFinancePaymentSaveReqVO.Item> list) {
        return convertList(list, o -> BeanUtils.toBean(o, ErpFinancePaymentItemDO.class, item -> {
            if (ObjectUtil.equal(item.getBizType(), ErpBizTypeEnum.PURCHASE_IN.getType())) {
                ErpPurchaseInDO purchaseIn = purchaseInService.validatePurchaseIn(item.getBizId());
                Assert.equals(purchaseIn.getSupplierId(), supplierId, "供应商必须相同");
                item.setTotalPrice(purchaseIn.getTotalPrice()).setBizNo(purchaseIn.getNo());
            } else if (ObjectUtil.equal(item.getBizType(), ErpBizTypeEnum.PURCHASE_RETURN.getType())) {
                ErpPurchaseReturnDO purchaseReturn = purchaseReturnService.validatePurchaseReturn(item.getBizId());
                Assert.equals(purchaseReturn.getSupplierId(), supplierId, "供应商必须相同");
                item.setTotalPrice(purchaseReturn.getTotalPrice().negate()).setBizNo(purchaseReturn.getNo());
            } else {
                throw new IllegalArgumentException("业务类型不正确：" + item.getBizType());
            }
        }));
    }

    private void updateFinancePaymentItemList(Long id, List<ErpFinancePaymentItemDO> newList) {
        // 第一步，对比新老数据，获得添加、修改、删除的列表
        List<ErpFinancePaymentItemDO> oldList = financePaymentItemMapper.selectListByPaymentId(id);
        List<List<ErpFinancePaymentItemDO>> diffList = diffList(oldList, newList, // id 不同，就认为是不同的记录
                (oldVal, newVal) -> oldVal.getId().equals(newVal.getId()));

        // 第二步，批量添加、修改、删除
        if (CollUtil.isNotEmpty(diffList.get(0))) {
            diffList.get(0).forEach(o -> o.setPaymentId(id));
            financePaymentItemMapper.insertBatch(diffList.get(0));
        }
        if (CollUtil.isNotEmpty(diffList.get(1))) {
            financePaymentItemMapper.updateBatch(diffList.get(1));
        }
        if (CollUtil.isNotEmpty(diffList.get(2))) {
            financePaymentItemMapper.deleteBatchIds(convertList(diffList.get(2), ErpFinancePaymentItemDO::getId));
        }

        // 第三步，更新采购入库、退货的付款金额情况
        updatePurchasePrice(CollectionUtils.newArrayList(diffList));
    }

    private void updatePurchasePrice(List<ErpFinancePaymentItemDO> paymentItems) {
        paymentItems.forEach(paymentItem -> {
            BigDecimal totalPaymentPrice = financePaymentItemMapper.selectPaymentPriceSumByBizIdAndBizType(
                    paymentItem.getBizId(), paymentItem.getBizType());
            if (ErpBizTypeEnum.PURCHASE_IN.getType().equals(paymentItem.getBizType())) {
                purchaseInService.updatePurchaseInPaymentPrice(paymentItem.getBizId(), totalPaymentPrice);
            } else if (ErpBizTypeEnum.PURCHASE_RETURN.getType().equals(paymentItem.getBizType())) {
                purchaseReturnService.updatePurchaseReturnRefundPrice(paymentItem.getBizId(), totalPaymentPrice.negate());
            } else {
                throw new IllegalArgumentException("业务类型不正确：" + paymentItem.getBizType());
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFinancePayment(List<Long> ids) {
        // 1. 校验不处于已审批
        List<ErpFinancePaymentDO> payments = financePaymentMapper.selectBatchIds(ids);
        if (CollUtil.isEmpty(payments)) {
            return;
        }
        payments.forEach(payment -> {
            if (ErpAuditStatus.APPROVE.getStatus().equals(payment.getStatus())) {
                throw exception(FINANCE_PAYMENT_DELETE_FAIL_APPROVE, payment.getNo());
            }
        });

        // 2. 遍历删除，并记录操作日志
        payments.forEach(payment -> {
            // 2.1 删除付款单
            financePaymentMapper.deleteById(payment.getId());
        });
    }

    private ErpFinancePaymentDO validateFinancePaymentExists(Long id) {
        ErpFinancePaymentDO payment = financePaymentMapper.selectById(id);
        if (payment == null) {
            throw exception(FINANCE_PAYMENT_NOT_EXISTS);
        }
        return payment;
    }

    @Override
    public ErpFinancePaymentDO getFinancePayment(Long id) {
        return financePaymentMapper.selectById(id);
    }

    @Override
    public PageResult<ErpFinancePaymentDO> getFinancePaymentPage(ErpFinancePaymentPageReqVO pageReqVO) {
        return financePaymentMapper.selectPage(pageReqVO);
    }

    // ==================== 付款单项 ====================

    @Override
    public List<ErpFinancePaymentItemDO> getFinancePaymentItemListByPaymentId(Long paymentId) {
        return financePaymentItemMapper.selectListByPaymentId(paymentId);
    }

    @Override
    public List<ErpFinancePaymentItemDO> getFinancePaymentItemListByPaymentIds(Collection<Long> paymentIds) {
        if (CollUtil.isEmpty(paymentIds)) {
            return Collections.emptyList();
        }
        return financePaymentItemMapper.selectListByPaymentIds(paymentIds);
    }

    @Override
    public Map<String, Object> getPaymentStatistic(ErpFinancePaymentPageReqVO pageReqVO) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("totalPayment", 0);// 采购总金额
        resultMap.put("amountPaid", 0);// 已结算金额
        resultMap.put("unpaidAmount", 0);// 待结算金额
        resultMap.put("percentage", 0);// 结算百分比

        // 1. 统计采购总金额
        ErpPurchaseOrderPageReqVO purchaseOrderPageReqVO = new ErpPurchaseOrderPageReqVO();
        purchaseOrderPageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        purchaseOrderPageReqVO.setSupplierId(pageReqVO.getSupplierId());// 供应商编号
        PageResult<ErpPurchaseOrderDO> purchaseOrderPageResult = purchaseOrderService.getPurchaseOrderPage(purchaseOrderPageReqVO);
        BigDecimal totalPayment = purchaseOrderPageResult.getList()
                .stream()
                .filter(e -> !e.getDeleted())
                .map(e -> e.getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        resultMap.put("totalPayment", totalPayment);// 采购总金额

        // 2. 统计付款单已结算金额
        PageResult<ErpFinancePaymentDO> erpFinancePaymentDOPageResult = financePaymentMapper.selectPage(pageReqVO);
        BigDecimal amountPaid = erpFinancePaymentDOPageResult.getList()
                .stream()
                .filter(e -> !e.getDeleted())
                .map(ErpFinancePaymentDO::getPaymentPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        resultMap.put("amountPaid", amountPaid);// 已结算金额

        // 3. 统计待结算金额
        BigDecimal unpaidAmount = totalPayment.subtract(amountPaid);
        resultMap.put("unpaidAmount", unpaidAmount);// 待结算金额
        // 4. 结算百分比
        if (totalPayment.compareTo(BigDecimal.ZERO) == 0) {
            resultMap.put("percentage", 0);
        } else {
            resultMap.put("percentage", amountPaid.divide(totalPayment, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
        }

        return resultMap;
    }

}