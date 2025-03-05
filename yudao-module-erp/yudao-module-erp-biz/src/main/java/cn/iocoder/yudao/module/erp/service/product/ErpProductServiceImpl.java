package cn.iocoder.yudao.module.erp.service.product;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.io.FileUtils;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.*;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ProductProfitSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductSkcDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.productprofit.ProductProfitDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpCustomerDO;
import cn.iocoder.yudao.module.erp.dal.mysql.product.ErpProductMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.product.ErpProductSkcMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.product.ProductProfitMapper;
import cn.iocoder.yudao.module.erp.service.sale.ErpCustomerService;
import cn.iocoder.yudao.module.report.controller.admin.report.vo.ReportSaveReqVO;
import cn.iocoder.yudao.module.report.service.report.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 产品 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ErpProductServiceImpl implements ErpProductService {

    @Resource
    private ErpProductMapper productMapper;
    @Resource
    private ErpProductSkcMapper productSkcMapper;
    @Resource
    private ProductProfitMapper productProfitMapper;
    @Resource
    private ErpProductCategoryService productCategoryService;
    @Resource
    private ErpProductUnitService productUnitService;
    @Resource
    private ReportService reportService;
    @Resource
    private ErpCustomerService customerService;

    @Override
    @Transactional
    public Long createProduct(ProductSaveReqVO createReqVO) {
        // 校验货号是否已经存在
        validateProductBarCodeExists(createReqVO.getBarCode());
        // 插入商品信息
        ErpProductDO product = BeanUtils.toBean(createReqVO, ErpProductDO.class);
        productMapper.insert(product);

        // 新建商品尺码模板
        // 使用类加载器获取 resource下模板文件 的内容
        String templatePath = "template/ProductSize.json";
        String fileStr = FileUtils.readFileOfResourcePath(ErpProductServiceImpl.class, templatePath);
        String jsonStr = fileStr.replace("{{barCode}}", createReqVO.getBarCode());

        // 获取文件的路径（URL 转为路径）
        ReportSaveReqVO reportSaveReqVO = new ReportSaveReqVO();
        reportSaveReqVO.setId(createReqVO.getBarCode());
        reportSaveReqVO.setCode(createReqVO.getBarCode());
        reportSaveReqVO.setName(createReqVO.getBarCode() + "尺码表");
        reportSaveReqVO.setType("datainfo");// 数据报表
        reportSaveReqVO.setJsonStr(jsonStr);
        reportSaveReqVO.setCreateBy("sys");
        reportSaveReqVO.setDelFlag(false);
        reportSaveReqVO.setTemplate(false);
        // 插入尺码报表
        reportService.createReport(reportSaveReqVO);

        // 返回
        return product.getId();
    }

    @Override
    public void updateProduct(ProductSaveReqVO updateReqVO) {
        // 校验存在
        validateProductExists(updateReqVO.getId());
        // 校验销售平台
        List<ErpCustomerDO> erpCustomerDOList = customerService.validateCustomerList(updateReqVO.getCustomerIdList());
        // 销售平台转字符串
        String customerIds = CollectionUtil.isNotEmpty(erpCustomerDOList)
                ? erpCustomerDOList.stream().map(ErpCustomerDO::getId).map(String::valueOf).collect(Collectors.joining(","))
                : "";

        // 更新
        ErpProductDO updateObj = BeanUtils.toBean(updateReqVO, ErpProductDO.class,
                erpProductDO -> erpProductDO.setCustomerIds(customerIds));
        productMapper.updateById(updateObj);
    }

    @Override
    public void deleteProduct(Long id) {
        // 校验存在
        validateProductExists(id);
        // 删除
        productMapper.deleteById(id);
    }

    @Override
    public List<ErpProductDO> validProductList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<ErpProductDO> list = productMapper.selectBatchIds(ids);
        Map<Long, ErpProductDO> productMap = convertMap(list, ErpProductDO::getId);
        for (Long id : ids) {
            ErpProductDO product = productMap.get(id);
            if (productMap.get(id) == null) {
                throw exception(PRODUCT_NOT_EXISTS);
            }
            if (CommonStatusEnum.isDisable(product.getStatus())) {
                throw exception(PRODUCT_NOT_ENABLE, product.getName());
            }
        }
        return list;
    }

    private void validateProductExists(Long id) {
        if (productMapper.selectById(id) == null) {
            throw exception(PRODUCT_NOT_EXISTS);
        }
    }

    private void validateProductBarCodeExists(String barCode) {
        List<ErpProductDO> list = productMapper.selectListByBarCode(barCode);
        if (list != null && !list.isEmpty()) {
            throw exception(PRODUCT_BARCODE_EXISTS);
        }
    }

    @Override
    public ErpProductDO getProduct(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public List<ErpProductDO> getProductBySpuBarCodeSet(Set<String> barCodeSet) {
        return productMapper.getProductBySpuBarCodeSet(barCodeSet);
    }

    @Override
    public List<ErpProductRespVO> getProductVOListByStatus(Integer status) {
        List<ErpProductDO> list = productMapper.selectListByStatus(status);
        return buildProductVOList(list);
    }

    @Override
    public List<ErpProductRespVO> getProductVOList() {
        List<ErpProductDO> list = productMapper.selectList();
        return buildProductVOList(list);
    }

    @Override
    public List<ErpProductRespVO> getProductVOList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<ErpProductDO> list = productMapper.selectBatchIds(ids);
        return buildProductVOList(list);
    }

    @Override
    public PageResult<ErpProductRespVO> getProductVOPage(ErpProductPageReqVO pageReqVO) {
        PageResult<ErpProductDO> pageResult = productMapper.selectPage(pageReqVO);
        return new PageResult<>(buildProductVOList(pageResult.getList()), pageResult.getTotal());
    }

    private List<ErpProductRespVO> buildProductVOList(List<ErpProductDO> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
//        Map<Long, ErpProductCategoryDO> categoryMap = productCategoryService.getProductCategoryMap(
//                convertSet(list, ErpProductDO::getCategoryId));
//        Map<Long, ErpProductUnitDO> unitMap = productUnitService.getProductUnitMap(
//                convertSet(list, ErpProductDO::getUnitId));
//        return BeanUtils.toBean(list, ErpProductRespVO.class, product -> {
//            MapUtils.findAndThen(categoryMap, product.getCategoryId(),
//                    category -> product.setCategoryName(category.getName()));
//            MapUtils.findAndThen(unitMap, product.getUnitId(),
//                    unit -> product.setUnitName(unit.getName()));
//        });
        // 销售平台处理
        List<ErpProductRespVO> result = BeanUtils.toBean(list, ErpProductRespVO.class, product -> {
            if (StrUtil.isNotBlank(product.getCustomerIds())) {
                List<Long> customerIdList = Arrays.stream(product.getCustomerIds().split(","))
                        .map(Long::valueOf)
                        .collect(Collectors.toList());

                product.setCustomerIdList(customerIdList);
            }
        });

        return result;
    }

    @Override
    public Long getProductCountByCategoryId(Long categoryId) {
        return productMapper.selectCountByCategoryId(categoryId);
    }

    @Override
    public Long getProductCountByUnitId(Long unitId) {
        return productMapper.selectCountByUnitId(unitId);
    }

    @Override
    public Long createProductSkc(ProductSkcSaveReqVO createReqVO) {
        // 插入
        ErpProductSkcDO productSkc = BeanUtils.toBean(createReqVO, ErpProductSkcDO.class);
        productSkcMapper.insert(productSkc);
        // 返回
        return productSkc.getId();
    }

    @Override
    public void updateProductSkc(ProductSkcSaveReqVO updateReqVO) {
        // 校验存在
        validateProductSkcExists(updateReqVO.getId());
        // 更新
        ErpProductSkcDO updateObj = BeanUtils.toBean(updateReqVO, ErpProductSkcDO.class);
        productSkcMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductSkc(Long id) {
        // 校验存在
        validateProductSkcExists(id);
        // 删除
        productSkcMapper.deleteById(id);
    }

    @Override
    public ErpProductSkcDO getProductSkc(Long id) {
        return productSkcMapper.selectById(id);
    }

    @Override
    public List<ErpProductSkcRespVO> getProductSkcListByProductIds(Collection<Long> productIds) {
        List<ErpProductSkcDO> list = productSkcMapper.selectProductSkcListByProductIds(productIds);

        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }

        return BeanUtils.toBean(list, ErpProductSkcRespVO.class);
    }

    private void validateProductSkcExists(Long id) {
        if (productSkcMapper.selectById(id) == null) {
            throw exception(PRODUCT_SKC_NOT_EXISTS);
        }
    }

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

    private void validateProductProfitExists(Long id) {
        if (productProfitMapper.selectById(id) == null) {
            throw exception(PRODUCT_PROFIT_NOT_EXISTS);
        }
    }
}