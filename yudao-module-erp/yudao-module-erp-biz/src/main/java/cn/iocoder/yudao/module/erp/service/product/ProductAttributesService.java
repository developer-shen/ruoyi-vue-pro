package cn.iocoder.yudao.module.erp.service.product;

import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ProductAttributesPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ProductAttributesSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.productattributes.ProductAttributesDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * ERP 产品属性 Service 接口
 *
 * @author 沈飞宇
 */
public interface ProductAttributesService {

    /**
     * 创建ERP 产品属性
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductAttributes(@Valid ProductAttributesSaveReqVO createReqVO);

    /**
     * 更新ERP 产品属性
     *
     * @param updateReqVO 更新信息
     */
    void updateProductAttributes(@Valid ProductAttributesSaveReqVO updateReqVO);

    /**
     * 删除ERP 产品属性
     *
     * @param id 编号
     */
    void deleteProductAttributes(Long id);

    /**
     * 获得ERP 产品属性
     *
     * @param id 编号
     * @return ERP 产品属性
     */
    ProductAttributesDO getProductAttributes(Long id);

    /**
     * 获得ERP 产品属性分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 产品属性分页
     */
    PageResult<ProductAttributesDO> getProductAttributesPage(ProductAttributesPageReqVO pageReqVO);

    /**
     * 根据产品id查询产品属性信息
     * @param productIds 产品id集合
     * @return 产品属性列表
     */
    List<ProductAttributesDO> getProductAttributesByProductIds(Collection<Long> productIds);

}