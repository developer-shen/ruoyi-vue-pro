package cn.iocoder.yudao.module.erp.controller.admin.sale.vo.order;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * 销售订单 Excel 导入 VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = false) // 设置 chain = false，避免导入有问题
public class ErpSaleOrderImportExcelVO {

    @ExcelProperty("订单时间")
    private LocalDate orderTime;

    @ExcelProperty("商品数量")
    private Integer productCount;

    @ExcelProperty("销售平台")
    private String customerName;

    @ExcelProperty("商品货号")
    private String productBarCode;

}
