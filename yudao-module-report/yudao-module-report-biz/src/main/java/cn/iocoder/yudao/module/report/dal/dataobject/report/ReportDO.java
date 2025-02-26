package cn.iocoder.yudao.module.report.dal.dataobject.report;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 在线excel设计器 DO
 *
 * @author 沈飞宇
 */
@TableName("jimu_report")
@KeySequence("jimu_report_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.INPUT)
    private String id;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 说明
     */
    private String note;
    /**
     * 状态
     */
    private String status;
    /**
     * 类型
     */
    private String type;
    /**
     * json字符串
     */
    private String jsonStr;
    /**
     * 请求地址
     */
    private String apiUrl;
    /**
     * 缩略图
     */
    private String thumb;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 删除标识0-正常,1-已删除
     */
    private Boolean delFlag;
    /**
     * 请求方法0-get,1-post
     */
    private String apiMethod;
    /**
     * 请求编码
     */
    private String apiCode;
    /**
     * 是否是模板 0-是,1-不是
     */
    private Boolean template;
    /**
     * 浏览次数
     */
    private Long viewCount;
    /**
     * css增强
     */
    private String cssStr;
    /**
     * js增强
     */
    private String jsStr;
    /**
     * py增强
     */
    private String pyStr;
    /**
     * 乐观锁版本
     */
    private Integer updateCount;
    /**
     * 是否填报报表 0不是,1是
     */
    private Boolean submitForm;

}