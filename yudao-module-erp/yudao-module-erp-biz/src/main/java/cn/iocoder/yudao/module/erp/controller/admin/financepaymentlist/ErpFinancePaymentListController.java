package cn.iocoder.yudao.module.erp.controller.admin.financepaymentlist;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.number.NumberUtils;
import cn.iocoder.yudao.module.erp.controller.admin.finance.vo.payment.ErpFinancePaymentRespVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.ErpAccountDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.ErpFinancePaymentDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.ErpFinancePaymentItemDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpSupplierDO;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
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
import java.util.stream.Stream;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.*;

import cn.iocoder.yudao.module.erp.controller.admin.financepaymentlist.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.financepaymentlist.ErpFinancePaymentListDO;
import cn.iocoder.yudao.module.erp.service.financepaymentlist.ErpFinancePaymentListService;

@Tag(name = "管理后台 - ERP 付款清单")
@RestController
@RequestMapping("/erp/finance-payment-list")
@Validated
public class ErpFinancePaymentListController {

    @Resource
    private ErpFinancePaymentListService financePaymentListService;
    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 付款清单")
    @PreAuthorize("@ss.hasPermission('erp:finance-payment-list:create')")
    public CommonResult<Long> createFinancePaymentList(@Valid @RequestBody ErpFinancePaymentListSaveReqVO createReqVO) {
        return success(financePaymentListService.createFinancePaymentList(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 付款清单")
    @PreAuthorize("@ss.hasPermission('erp:finance-payment-list:update')")
    public CommonResult<Boolean> updateFinancePaymentList(@Valid @RequestBody ErpFinancePaymentListSaveReqVO updateReqVO) {
        financePaymentListService.updateFinancePaymentList(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 付款清单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:finance-payment-list:delete')")
    public CommonResult<Boolean> deleteFinancePaymentList(@RequestParam("id") Long id) {
        financePaymentListService.deleteFinancePaymentList(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 付款清单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:finance-payment-list:query')")
    public CommonResult<ErpFinancePaymentListRespVO> getFinancePaymentList(@RequestParam("id") Long id) {
        ErpFinancePaymentListDO financePaymentList = financePaymentListService.getFinancePaymentList(id);
        return success(BeanUtils.toBean(financePaymentList, ErpFinancePaymentListRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 付款清单分页")
    @PreAuthorize("@ss.hasPermission('erp:finance-payment-list:query')")
    public CommonResult<PageResult<ErpFinancePaymentListRespVO>> getFinancePaymentListPage(@Valid ErpFinancePaymentListPageReqVO pageReqVO) {
        PageResult<ErpFinancePaymentListDO> pageResult = financePaymentListService.getFinancePaymentListPage(pageReqVO);
        return success(buildFinancePaymentListVOPageResult(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 付款清单 Excel")
    @PreAuthorize("@ss.hasPermission('erp:finance-payment-list:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportFinancePaymentListExcel(@Valid ErpFinancePaymentListPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpFinancePaymentListRespVO> list = buildFinancePaymentListVOPageResult(financePaymentListService.getFinancePaymentListPage(pageReqVO)).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 付款清单.xls", "数据", ErpFinancePaymentListRespVO.class,list);

    }

    private PageResult<ErpFinancePaymentListRespVO> buildFinancePaymentListVOPageResult(PageResult<ErpFinancePaymentListDO> pageResult) {
        if (CollUtil.isEmpty(pageResult.getList())) {
            return PageResult.empty(pageResult.getTotal());
        }
        // 1. 管理员信息
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(convertListByFlatMap(pageResult.getList(),
                contact -> Stream.of(NumberUtils.parseLong(contact.getCreator()), contact.getFinanceUserId())));
        // 2. 开始拼接
        return BeanUtils.toBean(pageResult, ErpFinancePaymentListRespVO.class, payment -> {
            MapUtils.findAndThen(userMap, Long.parseLong(payment.getCreator()), user -> payment.setCreatorName(user.getNickname()));
            MapUtils.findAndThen(userMap, payment.getFinanceUserId(), user -> payment.setFinanceUserName(user.getNickname()));
        });
    }
}