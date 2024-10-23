package cn.iocoder.yudao.module.erp.service.statistics;

import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.sale.ErpSaleTimeSummaryRespVO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ERP 销售统计 Service 接口
 *
 * @author 芋道源码
 */
public interface ErpSaleStatisticsService {

    /**
     * 获得销售金额
     *
     * 计算逻辑：销售出库的金额 - 销售退货的金额
     *
     * @param beginTime >= 开始时间
     * @param endTime < 结束时间
     * @return 销售金额
     */
    BigDecimal getSalePrice(LocalDateTime beginTime, LocalDateTime endTime);


    /**
     * 获得平台销售量统计
     * @param count 日期范围
     * @return
     */
    List<ErpSaleTimeSummaryRespVO> getSaleNumSummary(Integer count);

    /**
     * 获得平台销售金额统计
     * @param count 日期范围
     * @return
     */
    List<ErpSaleTimeSummaryRespVO> getSaleMoneySummary(Integer count);

    /**
     * 获得skc销售量统计
     * @param count 日期范围
     * @param customerId 平台客户
     * @return
     */
    List<ErpSaleTimeSummaryRespVO> getSaleSkcNumSummaryOfCustomer(Integer count, Long customerId);

}
