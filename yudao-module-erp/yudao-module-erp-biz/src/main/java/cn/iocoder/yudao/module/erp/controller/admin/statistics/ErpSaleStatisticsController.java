package cn.iocoder.yudao.module.erp.controller.admin.statistics;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils;
import cn.iocoder.yudao.module.erp.controller.admin.financepaymentlist.vo.ErpFinancePaymentListPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.sale.vo.order.ErpSaleOrderPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.sale.ErpSaleSummaryRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.sale.ErpSaleTimeSummaryRespVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpCustomerDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpSaleOrderDO;
import cn.iocoder.yudao.module.erp.service.sale.ErpSaleOrderService;
import cn.iocoder.yudao.module.erp.service.statistics.ErpSaleStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cn.hutool.core.date.DatePattern.NORM_DATE_PATTERN;
import static cn.hutool.core.date.DatePattern.NORM_MONTH_PATTERN;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - ERP 销售统计")
@RestController
@RequestMapping("/erp/sale-statistics")
@Validated
public class ErpSaleStatisticsController {

    @Resource
    private ErpSaleStatisticsService saleStatisticsService;

    @GetMapping("/summary")
    @Operation(summary = "获得销售统计")
    @PreAuthorize("@ss.hasPermission('erp:statistics:query')")
    public CommonResult<ErpSaleSummaryRespVO> getSaleSummary() {
        LocalDateTime today = LocalDateTimeUtils.getToday();
        LocalDateTime yesterday = LocalDateTimeUtils.getYesterday();
        LocalDateTime month = LocalDateTimeUtils.getMonth();
        LocalDateTime year = LocalDateTimeUtils.getYear();
        ErpSaleSummaryRespVO summary = new ErpSaleSummaryRespVO()
                .setTodayPrice(saleStatisticsService.getSalePrice(today, null))
                .setYesterdayPrice(saleStatisticsService.getSalePrice(yesterday, today))
                .setMonthPrice(saleStatisticsService.getSalePrice(month, null))
                .setYearPrice(saleStatisticsService.getSalePrice(year, null));
        return success(summary);
    }

    @GetMapping("/num-time-summary")
    @Operation(summary = "获得销售订单时间段统计")
    @Parameter(name = "count", description = "时间段数量", example = "6")
    @PreAuthorize("@ss.hasPermission('erp:statistics:query')")
    public CommonResult<List<ErpSaleTimeSummaryRespVO>> getSaleNumTimeSummary(
            @RequestParam(value = "count", defaultValue = "30") Integer count) {

        List<ErpSaleTimeSummaryRespVO> summaryList = saleStatisticsService.getSaleNumSummary(count);
        return success(summaryList);
    }

    @GetMapping("/money-time-summary")
    @Operation(summary = "获得销售金额时间段统计")
    @Parameter(name = "count", description = "时间段数量", example = "6")
    @PreAuthorize("@ss.hasPermission('erp:statistics:query')")
    public CommonResult<List<ErpSaleTimeSummaryRespVO>> getSaleMoneyTimeSummary(
            @RequestParam(value = "count", defaultValue = "30") Integer count) {

        List<ErpSaleTimeSummaryRespVO> summaryList = saleStatisticsService.getSaleMoneySummary(count);
        return success(summaryList);
    }

    @GetMapping("/spu-num-time-summary")
    @Operation(summary = "获得spu销售订单时间段统计")
    @Parameter(name = "count", description = "时间段数量", example = "6")
    @PreAuthorize("@ss.hasPermission('erp:statistics:query')")
    public CommonResult<List<ErpSaleTimeSummaryRespVO>> getSaleSpuNumTimeSummary(
            @RequestParam(value = "count", defaultValue = "7") Integer count,
            @RequestParam(value = "customerId") Long customerId) {

        List<ErpSaleTimeSummaryRespVO> summaryList = saleStatisticsService.getSaleSpuNumSummaryOfCustomer(count, customerId);
        return success(summaryList);
    }

}
