package cn.iocoder.yudao.module.report.dal.mysql.report;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.report.controller.admin.report.vo.ReportPageReqVO;
import cn.iocoder.yudao.module.report.dal.dataobject.report.ReportDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 在线excel设计器 Mapper
 *
 * @author 沈飞宇
 */
@Mapper
public interface ReportMapper extends BaseMapperX<ReportDO> {

    default PageResult<ReportDO> selectPage(ReportPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ReportDO>()
                .eqIfPresent(ReportDO::getCode, reqVO.getCode())
                .likeIfPresent(ReportDO::getName, reqVO.getName())
                .eqIfPresent(ReportDO::getNote, reqVO.getNote())
                .eqIfPresent(ReportDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ReportDO::getType, reqVO.getType())
                .eqIfPresent(ReportDO::getJsonStr, reqVO.getJsonStr())
                .eqIfPresent(ReportDO::getApiUrl, reqVO.getApiUrl())
                .eqIfPresent(ReportDO::getThumb, reqVO.getThumb())
                .eqIfPresent(ReportDO::getCreateBy, reqVO.getCreateBy())
                .betweenIfPresent(ReportDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ReportDO::getUpdateBy, reqVO.getUpdateBy())
                .eqIfPresent(ReportDO::getDelFlag, reqVO.getDelFlag())
                .eqIfPresent(ReportDO::getApiMethod, reqVO.getApiMethod())
                .eqIfPresent(ReportDO::getApiCode, reqVO.getApiCode())
                .eqIfPresent(ReportDO::getTemplate, reqVO.getTemplate())
                .eqIfPresent(ReportDO::getViewCount, reqVO.getViewCount())
                .eqIfPresent(ReportDO::getCssStr, reqVO.getCssStr())
                .eqIfPresent(ReportDO::getJsStr, reqVO.getJsStr())
                .eqIfPresent(ReportDO::getPyStr, reqVO.getPyStr())
                .eqIfPresent(ReportDO::getUpdateCount, reqVO.getUpdateCount())
                .eqIfPresent(ReportDO::getSubmitForm, reqVO.getSubmitForm())
                .orderByDesc(ReportDO::getId));
    }

}