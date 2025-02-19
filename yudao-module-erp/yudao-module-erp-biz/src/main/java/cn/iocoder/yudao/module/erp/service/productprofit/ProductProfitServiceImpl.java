package cn.iocoder.yudao.module.erp.service.productprofit;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.productprofit.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productprofit.ProductProfitDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.productprofit.ProductProfitMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * ERP 产品利润 Service 实现类
 *
 * @author 沈飞宇
 */
@Service
@Validated
public class ProductProfitServiceImpl implements ProductProfitService {

    @Resource
    private ProductProfitMapper productProfitMapper;

    @Override
    public Long createProductProfit(ProductProfitSaveReqVO createReqVO) {
        // 插入
        ProductProfitDO productProfit = BeanUtils.toBean(createReqVO, ProductProfitDO.class);
        productProfitMapper.insert(productProfit);
        // 返回
        return productProfit.getId();
    }

    @Override
    public void updateProductProfit(ProductProfitSaveReqVO updateReqVO) {
        // 校验存在
        validateProductProfitExists(updateReqVO.getId());
        // 更新
        ProductProfitDO updateObj = BeanUtils.toBean(updateReqVO, ProductProfitDO.class);
        productProfitMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductProfit(Long id) {
        // 校验存在
        validateProductProfitExists(id);
        // 删除
        productProfitMapper.deleteById(id);
    }

    private void validateProductProfitExists(Long id) {
        if (productProfitMapper.selectById(id) == null) {
            throw exception(null);
        }
    }

    @Override
    public ProductProfitDO getProductProfit(Long id) {
        return productProfitMapper.selectById(id);
    }

    @Override
    public PageResult<ProductProfitDO> getProductProfitPage(ProductProfitPageReqVO pageReqVO) {
        return productProfitMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ProductProfitDO> getProfitListByProductIds(Collection<Long> productIds) {
        List<ProductProfitDO> list = productProfitMapper.selectProfitListByProductIds(productIds);

        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }

        return list;
    }

}