package cn.iocoder.yudao.module.erp.service.product;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductSkcDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMultiMap;

/**
 * ERP 产品 Service 接口
 *
 * @author 芋道源码
 */
public interface ErpProductService {

    /**
     * 创建产品
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProduct(@Valid ProductSaveReqVO createReqVO);

    /**
     * 更新产品
     *
     * @param updateReqVO 更新信息
     */
    void updateProduct(@Valid ProductSaveReqVO updateReqVO);

    /**
     * 删除产品
     *
     * @param id 编号
     */
    void deleteProduct(Long id);

    /**
     * 校验产品们的有效性
     *
     * @param ids 编号数组
     * @return 产品列表
     */
    List<ErpProductDO> validProductList(Collection<Long> ids);

    /**
     * 获得产品
     *
     * @param id 编号
     * @return 产品
     */
    ErpProductDO getProduct(Long id);

    /**
     * 获得指定状态的产品 VO 列表
     *
     * @param status 状态
     * @return 产品 VO 列表
     */
    List<ErpProductRespVO> getProductVOListByStatus(Integer status);

    /**
     * 获得产品 VO 列表
     *
     * @param ids 编号数组
     * @return 产品 VO 列表
     */
    List<ErpProductRespVO> getProductVOList(Collection<Long> ids);

    /**
     * 获得产品 VO Map
     *
     * @param ids 编号数组
     * @return 产品 VO Map
     */
    default Map<Long, ErpProductRespVO> getProductVOMap(Collection<Long> ids) {
        return convertMap(getProductVOList(ids), ErpProductRespVO::getId);
    }

    /**
     * 获得产品 VO 分页
     *
     * @param pageReqVO 分页查询
     * @return 产品分页
     */
    PageResult<ErpProductRespVO> getProductVOPage(ErpProductPageReqVO pageReqVO);

    /**
     * 基于产品分类编号，获得产品数量
     *
     * @param categoryId 产品分类编号
     * @return 产品数量
     */
    Long getProductCountByCategoryId(Long categoryId);

    /**
     * 基于产品单位编号，获得产品数量
     *
     * @param unitId 产品单位编号
     * @return 产品数量
     */
    Long getProductCountByUnitId(Long unitId);

    /**
     * 创建产品变种skc
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductSkc(@Valid ProductSkcSaveReqVO createReqVO);

    /**
     * 更新产品变种skc
     *
     * @param updateReqVO 更新信息
     */
    void updateProductSkc(@Valid ProductSkcSaveReqVO updateReqVO);

    /**
     * 删除产品变种skc
     *
     * @param id 编号
     */
    void deleteProductSkc(Long id);

    /**
     * 获得产品
     *
     * @param id 编号
     * @return 产品
     */
    ErpProductSkcDO getProductSkc(Long id);

    /**
     * 根据产品id查询变种skc列表
     * @param productIds 产品id集合
     * @return 变种skc列表
     */
    List<ErpProductSkcRespVO> getProductSkcListByProductIds(Collection<Long> productIds);

    /**
     * 获得产品变种skc VO Map
     *
     * @param productIds 编号数组
     * @return 产品变种skc VO Map
     */
    default Map<Long, List<ErpProductSkcRespVO>> getProductSkcVOMap(Collection<Long> productIds) {
        return convertMultiMap(getProductSkcListByProductIds(productIds), ErpProductSkcRespVO::getProductId);
    }

}