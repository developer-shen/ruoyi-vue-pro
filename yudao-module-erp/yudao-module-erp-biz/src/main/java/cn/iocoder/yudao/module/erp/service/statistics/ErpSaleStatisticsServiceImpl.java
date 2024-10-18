package cn.iocoder.yudao.module.erp.service.statistics;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.erp.controller.admin.sale.vo.customer.ErpCustomerPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.sale.vo.order.ErpSaleOrderPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.sale.ErpSaleTimeSummaryRespVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpCustomerDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpSaleOrderDO;
import cn.iocoder.yudao.module.erp.dal.mysql.statistics.ErpSaleStatisticsMapper;
import cn.iocoder.yudao.module.erp.service.sale.ErpCustomerService;
import cn.iocoder.yudao.module.erp.service.sale.ErpSaleOrderService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.hutool.core.date.DatePattern.NORM_DATE_PATTERN;

/**
 * ERP 销售统计 Service 实现类
 *
 * @author 芋道源码
 */
@Service
public class ErpSaleStatisticsServiceImpl implements ErpSaleStatisticsService {

    @Resource
    private ErpSaleStatisticsMapper saleStatisticsMapper;

    @Resource
    private ErpSaleOrderService saleOrderService;

    @Resource
    private ErpCustomerService customerService;

    @Override
    public BigDecimal getSalePrice(LocalDateTime beginTime, LocalDateTime endTime) {
        return saleStatisticsMapper.getSalePrice(beginTime, endTime);
    }

    /**
     * 获得平台销售量统计
     *
     * @param count 日期范围
     * @return
     */
    @Override
    public List<ErpSaleTimeSummaryRespVO> getSaleSummaryOfCustomer(Integer count) {

        List<ErpSaleTimeSummaryRespVO> summaryList = new ArrayList<>();

        // 获取全部客户名称
        ErpCustomerPageReqVO customerPageReqVO = new ErpCustomerPageReqVO();
        List<ErpCustomerDO> customerList = customerService.getCustomerPage(customerPageReqVO).getList();

        // 获取全部销售订单
        ErpSaleOrderPageReqVO saleOrderPageReqVO = new ErpSaleOrderPageReqVO();
        saleOrderPageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpSaleOrderDO> allSaleOrderList = saleOrderService.getSaleOrderPage(saleOrderPageReqVO).getList();


            for (int i = count - 1; i >= 0; i--) {
                // 订单日期
                LocalDateTime startTime = LocalDateTimeUtil.beginOfDay(LocalDateTime.now().minusDays(i));
                ErpSaleTimeSummaryRespVO erpSaleTimeSummaryRespVO = new ErpSaleTimeSummaryRespVO();
                erpSaleTimeSummaryRespVO.setTime(LocalDateTimeUtil.format(startTime, NORM_DATE_PATTERN));

                // 平台及订单数量
                Map<String, Object> dataMap = new HashMap<>();
                for (ErpCustomerDO customer : customerList) {
                    // 订单日期的订单列表
                    List<ErpSaleOrderDO> saleOrderList = allSaleOrderList.stream()
                            .filter(e -> e.getCustomerId().equals(customer.getId())
                                    && e.getOrderTime().isEqual(startTime))
                            .collect(Collectors.toList());
                    // 订单数量
                    int saleNum = CollectionUtils.isEmpty(saleOrderList) ? 0 : saleOrderList.stream().mapToInt(e -> e.getTotalCount().intValue()).sum();

                    dataMap.put(customer.getName(), saleNum);
                }
                erpSaleTimeSummaryRespVO.setData(dataMap);
                summaryList.add(erpSaleTimeSummaryRespVO);
            }

        return summaryList;
    }

}
