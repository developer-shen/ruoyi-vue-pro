package cn.iocoder.yudao.module.erp.dal.mysql.financepaymentlist;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.financepaymentlist.ErpFinancePaymentListDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.financepaymentlist.vo.*;

/**
 * ERP 付款清单 Mapper
 *
 * @author 沈飞宇
 */
@Mapper
public interface ErpFinancePaymentListMapper extends BaseMapperX<ErpFinancePaymentListDO> {

    default PageResult<ErpFinancePaymentListDO> selectPage(ErpFinancePaymentListPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ErpFinancePaymentListDO>()
                .eqIfPresent(ErpFinancePaymentListDO::getFinanceUserId, reqVO.getFinanceUserId())
                .betweenIfPresent(ErpFinancePaymentListDO::getPaymentTime, reqVO.getPaymentTime())
                .eqIfPresent(ErpFinancePaymentListDO::getPaymentPrice, reqVO.getPaymentPrice())
                .eqIfPresent(ErpFinancePaymentListDO::getPaymentWay, reqVO.getPaymentWay())
                .eqIfPresent(ErpFinancePaymentListDO::getPaymentPurpose, reqVO.getPaymentPurpose())
                .eqIfPresent(ErpFinancePaymentListDO::getFileUrl, reqVO.getFileUrl())
                .eqIfPresent(ErpFinancePaymentListDO::getRemark, reqVO.getRemark())
                .eqIfPresent(ErpFinancePaymentListDO::getCreator, reqVO.getCreator())
                .betweenIfPresent(ErpFinancePaymentListDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ErpFinancePaymentListDO::getPaymentTime));
    }

}