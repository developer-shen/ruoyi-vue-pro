package cn.iocoder.yudao.module.erp.controller.admin.product;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.*;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ProductAttributesRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ProductAttributesSaveReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ProductProfitRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ProductProfitSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductSkcDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.productattributes.ProductAttributesDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.productprofit.ProductProfitDO;
import cn.iocoder.yudao.module.erp.service.product.ErpProductService;
import cn.iocoder.yudao.module.erp.service.product.ProductAttributesService;
import cn.iocoder.yudao.module.erp.service.product.ProductProfitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.*;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;

@Tag(name = "管理后台 - ERP 产品")
@RestController
@RequestMapping("/erp/product")
@Validated
public class ErpProductController {

    @Resource
    private ErpProductService productService;

    @Resource
    private ProductProfitService productProfitService;

    @Resource
    private ProductAttributesService productAttributesService;

    @PostMapping("/create")
    @Operation(summary = "创建产品")
    @PreAuthorize("@ss.hasPermission('erp:product:create')")
    public CommonResult<Long> createProduct(@Valid @RequestBody ProductSaveReqVO createReqVO) {
        return success(productService.createProduct(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新产品")
    @PreAuthorize("@ss.hasPermission('erp:product:update')")
    public CommonResult<Boolean> updateProduct(@Valid @RequestBody ProductSaveReqVO updateReqVO) {
        productService.updateProduct(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除产品")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:product:delete')")
    public CommonResult<Boolean> deleteProduct(@RequestParam("id") Long id) {
        productService.deleteProduct(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得产品")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:product:query')")
    public CommonResult<ErpProductRespVO> getProduct(@RequestParam("id") Long id) {
        ErpProductDO product = productService.getProduct(id);
        return success(BeanUtils.toBean(product, ErpProductRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得产品分页")
    @PreAuthorize("@ss.hasPermission('erp:product:query')")
    public CommonResult<PageResult<ErpProductRespVO>> getProductPage(@Valid ErpProductPageReqVO pageReqVO) {
        PageResult<ErpProductRespVO> pageResult = productService.getProductVOPage(pageReqVO);
        return success(buildProductVOPageResult(pageResult));
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得产品精简列表", description = "只包含被开启的产品，主要用于前端的下拉选项")
    public CommonResult<List<ErpProductRespVO>> getProductSimpleList() {
        List<ErpProductRespVO> list = productService.getProductVOListByStatus(CommonStatusEnum.ENABLE.getStatus());
        return success(convertList(list, product -> new ErpProductRespVO().setId(product.getId())
                .setName(product.getName()).setBarCode(product.getBarCode())
                .setCategoryId(product.getCategoryId()).setCategoryName(product.getCategoryName())
                .setUnitId(product.getUnitId()).setUnitName(product.getUnitName())
                .setPurchasePrice(product.getPurchasePrice()).setSalePrice(product.getSalePrice()).setMinPrice(product.getMinPrice())));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出产品 Excel")
    @PreAuthorize("@ss.hasPermission('erp:product:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductExcel(@Valid ErpProductPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        PageResult<ErpProductRespVO> pageResult = productService.getProductVOPage(pageReqVO);
        // 导出 Excel
        ExcelUtils.write(response, "产品.xlsx", "数据", ErpProductRespVO.class,
                pageResult.getList());
    }

    @PostMapping("/createSkc")
    @Operation(summary = "创建产品skc变种")
    @PreAuthorize("@ss.hasPermission('erp:product:create')")
    public CommonResult<Long> createProductSkc(@Valid @RequestBody ProductSkcSaveReqVO createReqVO) {
        return success(productService.createProductSkc(createReqVO));
    }

    @PutMapping("/updateSkc")
    @Operation(summary = "更新产品变种Skc")
    @PreAuthorize("@ss.hasPermission('erp:product:update')")
    public CommonResult<Boolean> updateProductSkc(@Valid @RequestBody ProductSkcSaveReqVO updateReqVO) {
        productService.updateProductSkc(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/deleteSkc")
    @Operation(summary = "删除产品变种Skc")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:product:delete')")
    public CommonResult<Boolean> deleteProductSkc(@RequestParam("id") Long id) {
        productService.deleteProductSkc(id);
        return success(true);
    }

    @GetMapping("/getSkc")
    @Operation(summary = "获得产品变种skc")
    @Parameter(name = "id", description = "skc编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:product:query')")
    public CommonResult<ErpProductSkcRespVO> getProductSkc(@RequestParam("id") Long id) {
        ErpProductSkcDO productSkc = productService.getProductSkc(id);
        return success(BeanUtils.toBean(productSkc, ErpProductSkcRespVO.class));
    }

    @GetMapping("/getProfit")
    @Operation(summary = "获得产品利润")
    @Parameter(name = "id", description = "利润编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:product:query')")
    public CommonResult<ProductProfitRespVO> getProductProfit(@RequestParam("id") Long id) {
        ProductProfitDO productProfit = productProfitService.getProductProfit(id);
        return success(BeanUtils.toBean(productProfit, ProductProfitRespVO.class));
    }

    @PostMapping("/createProfit")
    @Operation(summary = "创建产品利润")
    @PreAuthorize("@ss.hasPermission('erp:product:create')")
    public CommonResult<Long> createProductProfit(@Valid @RequestBody ProductProfitSaveReqVO createReqVO) {
        return success(productService.createProductProfit(createReqVO));
    }

    @PutMapping("/updateProfit")
    @Operation(summary = "更新产品利润")
    @PreAuthorize("@ss.hasPermission('erp:product:update')")
    public CommonResult<Boolean> updateProductProfit(@Valid @RequestBody ProductProfitSaveReqVO updateReqVO) {
        productService.updateProductProfit(updateReqVO);
        return success(true);
    }

    @PostMapping("/createAttr")
    @Operation(summary = "创建产品属性")
    @PreAuthorize("@ss.hasPermission('erp:product:create')")
    public CommonResult<Long> createProductAttr(@Valid @RequestBody ProductAttributesSaveReqVO createReqVO) {
        return success(productAttributesService.createProductAttributes(createReqVO));
    }

    @PutMapping("/updateAttr")
    @Operation(summary = "更新产品属性")
    @PreAuthorize("@ss.hasPermission('erp:product:update')")
    public CommonResult<Boolean> updateProductAttr(@Valid @RequestBody ProductAttributesSaveReqVO updateReqVO) {
        productAttributesService.updateProductAttributes(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/deleteAttr")
    @Operation(summary = "删除产品属性")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:product:delete')")
    public CommonResult<Boolean> deleteProductAttr(@RequestParam("id") Long id) {
        productAttributesService.deleteProductAttributes(id);
        return success(true);
    }

    @GetMapping("/getAttr")
    @Operation(summary = "获得产品属性")
    @Parameter(name = "id", description = "属性编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:product:query')")
    public CommonResult<ProductAttributesRespVO> getProductAttr(@RequestParam("id") Long id) {
        ProductAttributesDO productAttr = productAttributesService.getProductAttributes(id);
        return success(BeanUtils.toBean(productAttr, ProductAttributesRespVO.class));
    }

    private PageResult<ErpProductRespVO> buildProductVOPageResult(PageResult<ErpProductRespVO> pageResult) {
        if (CollUtil.isEmpty(pageResult.getList())) {
            return PageResult.empty(pageResult.getTotal());
        }
        // 1.1 产品变种SKC信息
        Map<Long, List<ErpProductSkcRespVO>> productSkcVOMap = productService.getProductSkcVOMap(
                convertSet(pageResult.getList(), ErpProductRespVO::getId));
        // 1.2 产品利润信息
        List<ProductProfitDO> profitListByProductIdList = productProfitService.getProfitListByProductIds(convertSet(pageResult.getList(), ErpProductRespVO::getId));
        // 1.3 产品属性信息
//        List<ProductAttributesDO> attributesListByProductIdList = productAttributesService.getProductAttributesByProductIds(convertSet(pageResult.getList(), ErpProductRespVO::getId));

        // 2. 开始拼接
        pageResult.getList().forEach(productVO -> {
            // 各产品的skc列表
            List<ErpProductSkcRespVO> productSkcList = productSkcVOMap.get(productVO.getId());
            // 2.1 拼接skc信息
            productVO.setItems(
                BeanUtils.toBean( productSkcList, ErpProductSkcRespVO.class)
            );
            productVO.setSkcCodes( CollUtil.join(productSkcList, "，", ErpProductSkcRespVO::getBarCode) );
            // 2.2填充预估利润信息
            List<ProductProfitDO> estimatedProfitList = profitListByProductIdList.stream().filter(profit -> profit.getProductId().equals(productVO.getId())).collect(Collectors.toList());
            if (estimatedProfitList != null && estimatedProfitList.size() > 0){
               productVO.setProfitId(estimatedProfitList.get(0).getId());
               productVO.setEstimatedProfit(estimatedProfitList.get(0).getProfit());
            }
            // 2.3 填充产品属性信息
//            List<ProductAttributesDO> attributesList = attributesListByProductIdList.stream().filter(attributes -> attributes.getProductId().equals(productVO.getId())).collect(Collectors.toList());
//            if (attributesList != null && attributesList.size() > 0){
//                productVO.setAttributesId(attributesList.get(0).getId());
//            }

        });

        return pageResult;
    }

}