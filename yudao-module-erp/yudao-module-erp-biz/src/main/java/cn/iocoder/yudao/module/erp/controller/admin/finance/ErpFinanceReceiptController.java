package cn.iocoder.yudao.module.erp.controller.admin.finance;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.number.NumberUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.erp.controller.admin.finance.vo.receipt.ErpFinanceReceiptPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.finance.vo.receipt.ErpFinanceReceiptRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.finance.vo.receipt.ErpFinanceReceiptSaveReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.financepaymentlist.vo.ErpFinancePaymentListRespVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.ErpAccountDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.ErpFinanceReceiptDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.ErpFinanceReceiptItemDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpCustomerDO;
import cn.iocoder.yudao.module.erp.service.finance.ErpAccountService;
import cn.iocoder.yudao.module.erp.service.finance.ErpFinanceReceiptService;
import cn.iocoder.yudao.module.erp.service.sale.ErpCustomerService;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.*;

@Tag(name = "管理后台 - ERP 收款单")
@RestController
@RequestMapping("/erp/finance-receipt")
@Validated
public class ErpFinanceReceiptController {

    @Resource
    private ErpFinanceReceiptService financeReceiptService;
    @Resource
    private ErpCustomerService customerService;
    @Resource
    private ErpAccountService accountService;

    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建收款单")
    @PreAuthorize("@ss.hasPermission('erp:finance-receipt:create')")
    public CommonResult<Long> createFinanceReceipt(@Valid @RequestBody ErpFinanceReceiptSaveReqVO createReqVO) {
        return success(financeReceiptService.createFinanceReceipt(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新收款单")
    @PreAuthorize("@ss.hasPermission('erp:finance-receipt:update')")
    public CommonResult<Boolean> updateFinanceReceipt(@Valid @RequestBody ErpFinanceReceiptSaveReqVO updateReqVO) {
        financeReceiptService.updateFinanceReceipt(updateReqVO);
        return success(true);
    }

    @PutMapping("/update-status")
    @Operation(summary = "更新收款单的状态")
    @PreAuthorize("@ss.hasPermission('erp:finance-receipt:update-status')")
    public CommonResult<Boolean> updateFinanceReceiptStatus(@RequestParam("id") Long id,
                                                           @RequestParam("status") Integer status) {
        financeReceiptService.updateFinanceReceiptStatus(id, status);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除收款单")
    @Parameter(name = "ids", description = "编号数组", required = true)
    @PreAuthorize("@ss.hasPermission('erp:finance-receipt:delete')")
    public CommonResult<Boolean> deleteFinanceReceipt(@RequestParam("ids") List<Long> ids) {
        financeReceiptService.deleteFinanceReceipt(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得收款单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:finance-receipt:query')")
    public CommonResult<ErpFinanceReceiptRespVO> getFinanceReceipt(@RequestParam("id") Long id) {
        ErpFinanceReceiptDO receipt = financeReceiptService.getFinanceReceipt(id);
        if (receipt == null) {
            return success(null);
        }
        return success(BeanUtils.toBean(receipt, ErpFinanceReceiptRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得收款单分页")
    @PreAuthorize("@ss.hasPermission('erp:finance-receipt:query')")
    public CommonResult<PageResult<ErpFinanceReceiptRespVO>> getFinanceReceiptPage(@Valid ErpFinanceReceiptPageReqVO pageReqVO) {
        PageResult<ErpFinanceReceiptDO> pageResult = financeReceiptService.getFinanceReceiptPage(pageReqVO);
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        PageResult<ErpFinanceReceiptDO> allData = financeReceiptService.getFinanceReceiptPage(pageReqVO);
        return success(buildFinanceReceiptVOPageResult(pageResult, allData));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出收款单 Excel")
    @PreAuthorize("@ss.hasPermission('erp:finance-receipt:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportFinanceReceiptExcel(@Valid ErpFinanceReceiptPageReqVO pageReqVO,
                                         HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpFinanceReceiptRespVO> list = buildFinanceReceiptVOPageResult(financeReceiptService.getFinanceReceiptPage(pageReqVO), null).getList();
        // 导出 Excel
        ExcelUtils.write(response, "收款单.xlsx", "数据", ErpFinanceReceiptRespVO.class, list);
    }

    private PageResult<ErpFinanceReceiptRespVO> buildFinanceReceiptVOPageResult(PageResult<ErpFinanceReceiptDO> pageResult, PageResult<ErpFinanceReceiptDO> allData) {
        if (CollUtil.isEmpty(pageResult.getList())) {
            return PageResult.empty(pageResult.getTotal());
        }
        // 1.1 收款平台信息
        Map<Long, ErpCustomerDO> customerMap = customerService.getCustomerMap(
                convertSet(pageResult.getList(), ErpFinanceReceiptDO::getCustomerId));
        // 1.2 结算账户信息
        Map<Long, ErpAccountDO> accountMap = accountService.getAccountMap(
                convertSet(pageResult.getList(), ErpFinanceReceiptDO::getAccountId));
        // 1.3 管理员信息
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(convertListByFlatMap(pageResult.getList(),
                contact -> Stream.of(NumberUtils.parseLong(contact.getCreator()), contact.getFinanceUserId())));
        // 2. 开始拼接
        PageResult<ErpFinanceReceiptRespVO> result = BeanUtils.toBean(pageResult, ErpFinanceReceiptRespVO.class, receipt -> {
            MapUtils.findAndThen(customerMap, receipt.getCustomerId(), customer -> receipt.setCustomerName(customer.getName()));
            MapUtils.findAndThen(accountMap, receipt.getAccountId(), account -> receipt.setAccountName(account.getName()));
            MapUtils.findAndThen(userMap, Long.parseLong(receipt.getCreator()), user -> receipt.setCreatorName(user.getNickname()));
            MapUtils.findAndThen(userMap, receipt.getFinanceUserId(), user -> receipt.setFinanceUserName(user.getNickname()));
        });

        // 3. 附加统计数据
        Map<String, Object> sideMap = new HashMap<>();
        if (allData!= null && !CollectionUtils.isEmpty(allData.getList())) {
            // 3.1 收款平台信息（全部数据）
            Map<Long, ErpCustomerDO> allCustomerMap = customerService.getCustomerMap(
                    convertSet(allData.getList(), ErpFinanceReceiptDO::getCustomerId));
            // 3.2. 开始拼接（全部数据）
            PageResult<ErpFinanceReceiptRespVO> allDataVO = BeanUtils.toBean(allData, ErpFinanceReceiptRespVO.class, receipt -> {
                MapUtils.findAndThen(allCustomerMap, receipt.getCustomerId(), customer -> receipt.setCustomerId(customer.getId()));
                MapUtils.findAndThen(allCustomerMap, receipt.getCustomerId(), customer -> receipt.setCustomerName(customer.getName()));
            });
            // 3.3. 统计饼状图1：合计收款数据
            List<Map<String, Object>> totalPricePieOptionsDataList = new ArrayList<>();
            allDataVO.getList().stream().map(ErpFinanceReceiptRespVO::getCustomerName).distinct().collect(Collectors.toList()).forEach(e -> {
                BigDecimal sumOfTotalPrice = allDataVO.getList()
                        .stream().filter(vo -> !StringUtils.isEmpty(e) && e.equals(vo.getCustomerName()))
                        .map(ErpFinanceReceiptRespVO::getTotalPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .setScale(2, BigDecimal.ROUND_HALF_UP);

                Map<String, Object> statisticsMap = new HashMap<>();
                statisticsMap.put("name", e + "");
                statisticsMap.put("value", sumOfTotalPrice);

                totalPricePieOptionsDataList.add(statisticsMap);
            });

            // 3.4. 饼状图2：实际到账数据
            List<Map<String, Object>> receiptPricePieOptionsDataList = new ArrayList<>();
            allDataVO.getList().stream().map(ErpFinanceReceiptRespVO::getCustomerName).distinct().collect(Collectors.toList()).forEach(e -> {
                BigDecimal sumOfReceiptPrice = allDataVO.getList()
                        .stream().filter(vo -> !StringUtils.isEmpty(e) && e.equals(vo.getCustomerName()))
                        .map(ErpFinanceReceiptRespVO::getReceiptPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .setScale(2, BigDecimal.ROUND_HALF_UP);

                Map<String, Object> statisticsMap = new HashMap<>();
                statisticsMap.put("name", e + "");
                statisticsMap.put("value", sumOfReceiptPrice);

                receiptPricePieOptionsDataList.add(statisticsMap);
            });

            // finally
            // 填充A:饼状图1：合计收款数据
            sideMap.put("totalPricePieOptionsDataList", totalPricePieOptionsDataList);
            // 填充A:饼状图2：实际到账数据
            sideMap.put("receiptPricePieOptionsDataList", receiptPricePieOptionsDataList);
        }
        result.setSide(sideMap);
        return result;
    }


}
