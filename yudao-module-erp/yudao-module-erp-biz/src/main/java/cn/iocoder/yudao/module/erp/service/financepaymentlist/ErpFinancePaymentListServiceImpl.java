package cn.iocoder.yudao.module.erp.service.financepaymentlist;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.financepaymentlist.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.financepaymentlist.ErpFinancePaymentListDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.financepaymentlist.ErpFinancePaymentListMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 付款清单 Service 实现类
 *
 * @author 沈飞宇
 */
@Service
@Validated
public class ErpFinancePaymentListServiceImpl implements ErpFinancePaymentListService {

    @Resource
    private ErpFinancePaymentListMapper financePaymentListMapper;

    @Override
    public Long createFinancePaymentList(ErpFinancePaymentListSaveReqVO createReqVO) {
        // 插入
        ErpFinancePaymentListDO financePaymentList = BeanUtils.toBean(createReqVO, ErpFinancePaymentListDO.class);
        financePaymentListMapper.insert(financePaymentList);
        // 返回
        return financePaymentList.getId();
    }

    @Override
    public void updateFinancePaymentList(ErpFinancePaymentListSaveReqVO updateReqVO) {
        // 校验存在
        validateFinancePaymentListExists(updateReqVO.getId());
        // 更新
        ErpFinancePaymentListDO updateObj = BeanUtils.toBean(updateReqVO, ErpFinancePaymentListDO.class);
        financePaymentListMapper.updateById(updateObj);
    }

    @Override
    public void deleteFinancePaymentList(Long id) {
        // 校验存在
        validateFinancePaymentListExists(id);
        // 删除
        financePaymentListMapper.deleteById(id);
    }

    private void validateFinancePaymentListExists(Long id) {
        if (financePaymentListMapper.selectById(id) == null) {
            throw exception(FINANCE_PAYMENT_LIST_NOT_EXISTS);
        }
    }

    @Override
    public ErpFinancePaymentListDO getFinancePaymentList(Long id) {
        return financePaymentListMapper.selectById(id);
    }

    @Override
    public PageResult<ErpFinancePaymentListDO> getFinancePaymentListPage(ErpFinancePaymentListPageReqVO pageReqVO) {
        return financePaymentListMapper.selectPage(pageReqVO);
    }

}