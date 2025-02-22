package cn.iocoder.yudao.module.erp.service.product;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ProductAttributesPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ProductAttributesSaveReqVO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;

import cn.iocoder.yudao.module.erp.dal.dataobject.productattributes.ProductAttributesDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.product.ProductAttributesMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 产品属性 Service 实现类
 *
 * @author 沈飞宇
 */
@Service
@Validated
public class ProductAttributesServiceImpl implements ProductAttributesService {

    @Resource
    private ProductAttributesMapper productAttributesMapper;

    @Override
    public Long createProductAttributes(ProductAttributesSaveReqVO createReqVO) {
        // 插入
        ProductAttributesDO productAttributes = BeanUtils.toBean(createReqVO, ProductAttributesDO.class);
        productAttributesMapper.insert(productAttributes);
        // 返回
        return productAttributes.getId();
    }

    @Override
    public void updateProductAttributes(ProductAttributesSaveReqVO updateReqVO) {
        // 校验存在
        validateProductAttributesExists(updateReqVO.getId());
        // 更新
        ProductAttributesDO updateObj = BeanUtils.toBean(updateReqVO, ProductAttributesDO.class);
        productAttributesMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductAttributes(Long id) {
        // 校验存在
        validateProductAttributesExists(id);
        // 删除
        productAttributesMapper.deleteById(id);
    }

    private void validateProductAttributesExists(Long id) {
        if (productAttributesMapper.selectById(id) == null) {
            throw exception(PRODUCT_ATTRIBUTES_NOT_EXISTS);
        }
    }

    @Override
    public ProductAttributesDO getProductAttributes(Long id) {
        return productAttributesMapper.selectById(id);
    }

    @Override
    public PageResult<ProductAttributesDO> getProductAttributesPage(ProductAttributesPageReqVO pageReqVO) {
        return productAttributesMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ProductAttributesDO> getProductAttributesByProductIds(Collection<Long> productIds) {
        List<ProductAttributesDO> list = productAttributesMapper.selectAttributesListByProductIds(productIds);

        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }

        return list;
    }

}