package cn.iocoder.yudao.module.erp.controller.admin.financepaymentlist;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.number.NumberUtils;
import cn.iocoder.yudao.module.erp.controller.admin.finance.vo.payment.ErpFinancePaymentRespVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.ErpAccountDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.ErpFinancePaymentDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.ErpFinancePaymentItemDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpSupplierDO;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.math.BigDecimal;
import java.util.*;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.*;

import cn.iocoder.yudao.module.erp.controller.admin.financepaymentlist.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.financepaymentlist.ErpFinancePaymentListDO;
import cn.iocoder.yudao.module.erp.service.financepaymentlist.ErpFinancePaymentListService;

@Tag(name = "管理后台 - ERP 付款清单")
@RestController
@RequestMapping("/erp/finance-payment-list")
@Validated
public class ErpFinancePaymentListController {

    @Resource
    private ErpFinancePaymentListService financePaymentListService;
    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 付款清单")
    @PreAuthorize("@ss.hasPermission('erp:finance-payment-list:create')")
    public CommonResult<Long> createFinancePaymentList(@Valid @RequestBody ErpFinancePaymentListSaveReqVO createReqVO) {
        return success(financePaymentListService.createFinancePaymentList(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 付款清单")
    @PreAuthorize("@ss.hasPermission('erp:finance-payment-list:update')")
    public CommonResult<Boolean> updateFinancePaymentList(@Valid @RequestBody ErpFinancePaymentListSaveReqVO updateReqVO) {
        financePaymentListService.updateFinancePaymentList(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 付款清单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:finance-payment-list:delete')")
    public CommonResult<Boolean> deleteFinancePaymentList(@RequestParam("id") Long id) {
        financePaymentListService.deleteFinancePaymentList(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 付款清单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:finance-payment-list:query')")
    public CommonResult<ErpFinancePaymentListRespVO> getFinancePaymentList(@RequestParam("id") Long id) {
        ErpFinancePaymentListDO financePaymentList = financePaymentListService.getFinancePaymentList(id);
        return success(BeanUtils.toBean(financePaymentList, ErpFinancePaymentListRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 付款清单分页")
    @PreAuthorize("@ss.hasPermission('erp:finance-payment-list:query')")
    public CommonResult<PageResult<ErpFinancePaymentListRespVO>> getFinancePaymentListPage(@Valid ErpFinancePaymentListPageReqVO pageReqVO) {
        PageResult<ErpFinancePaymentListDO> pageResult = financePaymentListService.getFinancePaymentListPage(pageReqVO);
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        PageResult<ErpFinancePaymentListDO> allData = financePaymentListService.getFinancePaymentListPage(pageReqVO);
        return success(buildFinancePaymentListVOPageResult(pageResult, allData));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 付款清单 Excel")
    @PreAuthorize("@ss.hasPermission('erp:finance-payment-list:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportFinancePaymentListExcel(@Valid ErpFinancePaymentListPageReqVO pageReqVO,
                                              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpFinancePaymentListRespVO> list = buildFinancePaymentListVOPageResult(financePaymentListService.getFinancePaymentListPage(pageReqVO), null).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 付款清单.xlsxx", "数据", ErpFinancePaymentListRespVO.class, list);

    }

    private PageResult<ErpFinancePaymentListRespVO> buildFinancePaymentListVOPageResult(PageResult<ErpFinancePaymentListDO> pageResult, PageResult<ErpFinancePaymentListDO> allData) {
        if (CollUtil.isEmpty(pageResult.getList())) {
            return PageResult.empty(pageResult.getTotal());
        }
        // 1. 管理员信息（分页数据）
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(convertListByFlatMap(pageResult.getList(),
                contact -> Stream.of(NumberUtils.parseLong(contact.getCreator()), contact.getFinanceUserId())));
        // 2. 开始拼接（分页数据）
        PageResult<ErpFinancePaymentListRespVO> result = BeanUtils.toBean(pageResult, ErpFinancePaymentListRespVO.class, payment -> {
            MapUtils.findAndThen(userMap, Long.parseLong(payment.getCreator()), user -> payment.setCreatorName(user.getNickname()));
            MapUtils.findAndThen(userMap, payment.getFinanceUserId(), user -> payment.setFinanceUserName(user.getNickname()));
        });
        // 3. 附加统计数据
        Map<String, Object> sideMap = new HashMap<>();
        if (allData!= null && !CollectionUtils.isEmpty(allData.getList())) {
            // A:饼状图数据
            List<Map<String, Object>> pieOptionsDataList = new ArrayList<>();
            // 3.1. 管理员信息（全部数据）
            Map<Long, AdminUserRespDTO> alluserMap = adminUserApi.getUserMap(convertListByFlatMap(allData.getList(),
                    contact -> Stream.of(NumberUtils.parseLong(contact.getCreator()), contact.getFinanceUserId())));
            // 3.2. 开始拼接（全部数据）
            PageResult<ErpFinancePaymentListRespVO> allDataVO = BeanUtils.toBean(allData, ErpFinancePaymentListRespVO.class, payment -> {
                MapUtils.findAndThen(alluserMap, Long.parseLong(payment.getCreator()), user -> payment.setCreatorName(user.getNickname()));
                MapUtils.findAndThen(alluserMap, payment.getFinanceUserId(), user -> payment.setFinanceUserName(user.getNickname()));
            });
            // 3.3. 统计饼状图数据
            allDataVO.getList().stream().map(ErpFinancePaymentListRespVO::getFinanceUserName).distinct().collect(Collectors.toList()).forEach(e -> {
                BigDecimal sumOfPaymentPrice = allDataVO.getList()
                        .stream().filter(vo -> !StringUtils.isEmpty(e) && e.equals(vo.getFinanceUserName()))
                        .map(ErpFinancePaymentListRespVO::getPaymentPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .setScale(2, BigDecimal.ROUND_HALF_UP);

                Map<String, Object> statisticsMap = new HashMap<>();
                statisticsMap.put("name", e + "");
                statisticsMap.put("value", sumOfPaymentPrice);

                pieOptionsDataList.add(statisticsMap);
            });

            // B:柱状图数据
            List<Map<String, Object>> barOptionsDataList = new ArrayList<>();
            // 3.4. 统计柱状图数据
            allDataVO.getList().stream().map(ErpFinancePaymentListRespVO::getPaymentPurpose).distinct().collect(Collectors.toList()).forEach(e -> {
                if(!StringUtils.isEmpty(e) && !e.equals("其他") ){
                    BigDecimal sumOfPaymentPrice = allDataVO.getList()
                            .stream().filter(vo -> e.equals(vo.getPaymentPurpose()))
                            .map(ErpFinancePaymentListRespVO::getPaymentPrice)
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
                            .setScale(2, BigDecimal.ROUND_HALF_UP);

                    Map<String, Object> statisticsMap = new HashMap<>();
                    statisticsMap.put("name", e + "");
                    statisticsMap.put("value", sumOfPaymentPrice);

                    barOptionsDataList.add(statisticsMap);
                }
            });
            // 3.5. 统计柱状图数据(无付款用途的数据)
            BigDecimal sumOfNonePaymentPrice = allDataVO.getList()
                   .stream().filter(vo -> StringUtils.isEmpty(vo.getPaymentPurpose()) || vo.getPaymentPurpose().equals("其他"))
                   .map(ErpFinancePaymentListRespVO::getPaymentPrice)
                   .reduce(BigDecimal.ZERO, BigDecimal::add)
                   .setScale(2, BigDecimal.ROUND_HALF_UP);

            if(sumOfNonePaymentPrice.intValue() != 0) {
                Map<String, Object> statisticsMapOfNonePaymentPrice = new HashMap<>();
                statisticsMapOfNonePaymentPrice.put("name", "其他");
                statisticsMapOfNonePaymentPrice.put("value", sumOfNonePaymentPrice);
                barOptionsDataList.add(statisticsMapOfNonePaymentPrice);
            }

            // 按照 value 进行排序（BigDecimal 类型）
            barOptionsDataList.sort((mapA, mapB) -> {
                BigDecimal valueA = (BigDecimal) mapA.get("value");
                BigDecimal valueB = (BigDecimal) mapB.get("value");
                return valueA.compareTo(valueB);
            });

            // finally
            // 填充A:饼状图数据
            sideMap.put("pieOptionsDataList", pieOptionsDataList);
            // 填充B:柱状图数据
            sideMap.put("barOptionsDataList", barOptionsDataList);
        }
        result.setSide(sideMap);
        return result;
    }
}