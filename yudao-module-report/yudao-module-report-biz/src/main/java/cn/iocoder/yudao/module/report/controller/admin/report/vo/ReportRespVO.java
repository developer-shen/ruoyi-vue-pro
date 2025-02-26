package cn.iocoder.yudao.module.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 在线excel设计器 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ReportRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "3018")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "编码")
    @ExcelProperty("编码")
    private String code;

    @Schema(description = "名称", example = "张三")
    @ExcelProperty("名称")
    private String name;

    @Schema(description = "说明")
    @ExcelProperty("说明")
    private String note;

    @Schema(description = "状态", example = "1")
    @ExcelProperty("状态")
    private String status;

    @Schema(description = "类型", example = "1")
    @ExcelProperty("类型")
    private String type;

    @Schema(description = "json字符串")
    @ExcelProperty("json字符串")
    private String jsonStr;

    @Schema(description = "请求地址", example = "https://www.iocoder.cn")
    @ExcelProperty("请求地址")
    private String apiUrl;

    @Schema(description = "缩略图")
    @ExcelProperty("缩略图")
    private String thumb;

    @Schema(description = "创建人")
    @ExcelProperty("创建人")
    private String createBy;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "修改人")
    @ExcelProperty("修改人")
    private String updateBy;

    @Schema(description = "删除标识0-正常,1-已删除")
    @ExcelProperty("删除标识0-正常,1-已删除")
    private Boolean delFlag;

    @Schema(description = "请求方法0-get,1-post")
    @ExcelProperty("请求方法0-get,1-post")
    private String apiMethod;

    @Schema(description = "请求编码")
    @ExcelProperty("请求编码")
    private String apiCode;

    @Schema(description = "是否是模板 0-是,1-不是")
    @ExcelProperty("是否是模板 0-是,1-不是")
    private Boolean template;

    @Schema(description = "浏览次数", example = "31214")
    @ExcelProperty("浏览次数")
    private Long viewCount;

    @Schema(description = "css增强")
    @ExcelProperty("css增强")
    private String cssStr;

    @Schema(description = "js增强")
    @ExcelProperty("js增强")
    private String jsStr;

    @Schema(description = "py增强")
    @ExcelProperty("py增强")
    private String pyStr;

    @Schema(description = "乐观锁版本", example = "21729")
    @ExcelProperty("乐观锁版本")
    private Integer updateCount;

    @Schema(description = "是否填报报表 0不是,1是")
    @ExcelProperty("是否填报报表 0不是,1是")
    private Boolean submitForm;

}