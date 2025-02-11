package cn.iocoder.yudao.module.erp.dal.mysql.product;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ErpProductPageReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductSkcDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * ERP 产品变种skc Mapper
 *
 * @author
 */
@Mapper
public interface ErpProductSkcMapper extends BaseMapperX<ErpProductSkcDO> {

    /**
     * 根据产品id查询变种skc列表
     * @param productIds 产品id集合
     * @return 变种skc列表
     */
    default List<ErpProductSkcDO> selectProductSkcListByProductIds(Collection<Long> productIds) {
        return selectList(ErpProductSkcDO::getProductId, productIds);
    }
}