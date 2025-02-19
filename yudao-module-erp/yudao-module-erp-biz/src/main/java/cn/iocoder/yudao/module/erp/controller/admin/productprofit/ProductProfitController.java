package cn.iocoder.yudao.module.erp.controller.admin.productprofit;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.erp.controller.admin.productprofit.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productprofit.ProductProfitDO;
import cn.iocoder.yudao.module.erp.service.productprofit.ProductProfitService;

@Tag(name = "管理后台 - ERP 产品利润")
@RestController
@RequestMapping("/erp/product-profit")
@Validated
public class ProductProfitController {

    @Resource
    private ProductProfitService productProfitService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 产品利润")
    @PreAuthorize("@ss.hasPermission('erp:product-profit:create')")
    public CommonResult<Long> createProductProfit(@Valid @RequestBody ProductProfitSaveReqVO createReqVO) {
        return success(productProfitService.createProductProfit(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 产品利润")
    @PreAuthorize("@ss.hasPermission('erp:product-profit:update')")
    public CommonResult<Boolean> updateProductProfit(@Valid @RequestBody ProductProfitSaveReqVO updateReqVO) {
        productProfitService.updateProductProfit(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 产品利润")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:product-profit:delete')")
    public CommonResult<Boolean> deleteProductProfit(@RequestParam("id") Long id) {
        productProfitService.deleteProductProfit(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 产品利润")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:product-profit:query')")
    public CommonResult<ProductProfitRespVO> getProductProfit(@RequestParam("id") Long id) {
        ProductProfitDO productProfit = productProfitService.getProductProfit(id);
        return success(BeanUtils.toBean(productProfit, ProductProfitRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 产品利润分页")
    @PreAuthorize("@ss.hasPermission('erp:product-profit:query')")
    public CommonResult<PageResult<ProductProfitRespVO>> getProductProfitPage(@Valid ProductProfitPageReqVO pageReqVO) {
        PageResult<ProductProfitDO> pageResult = productProfitService.getProductProfitPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProductProfitRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 产品利润 Excel")
    @PreAuthorize("@ss.hasPermission('erp:product-profit:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductProfitExcel(@Valid ProductProfitPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductProfitDO> list = productProfitService.getProductProfitPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 产品利润.xlsx", "数据", ProductProfitRespVO.class,
                        BeanUtils.toBean(list, ProductProfitRespVO.class));
    }

}