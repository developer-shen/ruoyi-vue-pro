package cn.iocoder.yudao.module.erp.service.financepaymentlist;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.financepaymentlist.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.financepaymentlist.ErpFinancePaymentListDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 付款清单 Service 接口
 *
 * @author 沈飞宇
 */
public interface ErpFinancePaymentListService {

    /**
     * 创建ERP 付款清单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createFinancePaymentList(@Valid ErpFinancePaymentListSaveReqVO createReqVO);

    /**
     * 更新ERP 付款清单
     *
     * @param updateReqVO 更新信息
     */
    void updateFinancePaymentList(@Valid ErpFinancePaymentListSaveReqVO updateReqVO);

    /**
     * 删除ERP 付款清单
     *
     * @param id 编号
     */
    void deleteFinancePaymentList(Long id);

    /**
     * 获得ERP 付款清单
     *
     * @param id 编号
     * @return ERP 付款清单
     */
    ErpFinancePaymentListDO getFinancePaymentList(Long id);

    /**
     * 获得ERP 付款清单分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 付款清单分页
     */
    PageResult<ErpFinancePaymentListDO> getFinancePaymentListPage(ErpFinancePaymentListPageReqVO pageReqVO);

}