package cn.iocoder.yudao.module.erp.dal.mysql.product;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ProductAttributesPageReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.productattributes.ProductAttributesDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * ERP 产品属性 Mapper
 *
 * @author 沈飞宇
 */
@Mapper
public interface ProductAttributesMapper extends BaseMapperX<ProductAttributesDO> {

    default PageResult<ProductAttributesDO> selectPage(ProductAttributesPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductAttributesDO>()
                .eqIfPresent(ProductAttributesDO::getProductId, reqVO.getProductId())
                .betweenIfPresent(ProductAttributesDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ProductAttributesDO::getRemark, reqVO.getRemark())
                .eqIfPresent(ProductAttributesDO::getWeight, reqVO.getWeight())
                .eqIfPresent(ProductAttributesDO::getSizes, reqVO.getSizes())
                .eqIfPresent(ProductAttributesDO::getStock, reqVO.getStock())
                .eqIfPresent(ProductAttributesDO::getComposition, reqVO.getComposition())
                .eqIfPresent(ProductAttributesDO::getProductMeasurements, reqVO.getProductMeasurements())
                .eqIfPresent(ProductAttributesDO::getBodyMeasurements, reqVO.getBodyMeasurements())
                .eqIfPresent(ProductAttributesDO::getCareInstructions, reqVO.getCareInstructions())
                .orderByDesc(ProductAttributesDO::getId));
    }

    /**
     * 根据产品id查询产品属性信息
     * @param productIds 产品id集合
     * @return 产品属性列表
     */
    default List<ProductAttributesDO> selectAttributesListByProductIds(Collection<Long> productIds) {
        return selectList(ProductAttributesDO::getProductId, productIds);
    }
}