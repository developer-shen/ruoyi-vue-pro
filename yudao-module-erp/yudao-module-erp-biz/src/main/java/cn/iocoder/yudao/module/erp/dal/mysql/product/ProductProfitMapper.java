package cn.iocoder.yudao.module.erp.dal.mysql.product;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ProductProfitPageReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.productprofit.ProductProfitDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * ERP 产品利润 Mapper
 *
 * @author 沈飞宇
 */
@Mapper
public interface ProductProfitMapper extends BaseMapperX<ProductProfitDO> {

    default PageResult<ProductProfitDO> selectPage(ProductProfitPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductProfitDO>()
                .eqIfPresent(ProductProfitDO::getProductId, reqVO.getProductId())
                .eqIfPresent(ProductProfitDO::getProfit, reqVO.getProfit())
                .eqIfPresent(ProductProfitDO::getPurchasePrice, reqVO.getPurchasePrice())
                .eqIfPresent(ProductProfitDO::getSalePrice, reqVO.getSalePrice())
                .eqIfPresent(ProductProfitDO::getFirstLegPrice, reqVO.getFirstLegPrice())
                .eqIfPresent(ProductProfitDO::getLastMilePrice, reqVO.getLastMilePrice())
                .eqIfPresent(ProductProfitDO::getRefundRate, reqVO.getRefundRate())
                .eqIfPresent(ProductProfitDO::getRefundFreight, reqVO.getRefundFreight())
                .eqIfPresent(ProductProfitDO::getOtherPrice, reqVO.getOtherPrice())
                .eqIfPresent(ProductProfitDO::getOtherDetail, reqVO.getOtherDetail())
                .betweenIfPresent(ProductProfitDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ProductProfitDO::getRemark, reqVO.getRemark())
                .orderByDesc(ProductProfitDO::getId));
    }


    /**
     * 根据产品id查询产品利润信息
     * @param productIds 产品id集合
     * @return 变种skc列表
     */
    default List<ProductProfitDO> selectProfitListByProductIds(Collection<Long> productIds) {
        return selectList(ProductProfitDO::getProductId, productIds);
    }

}