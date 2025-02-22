package cn.iocoder.yudao.module.erp.service.product;

import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ProductProfitPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ProductProfitSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.productprofit.ProductProfitDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * ERP 产品利润 Service 接口
 *
 * @author 沈飞宇
 */
public interface ProductProfitService {

    /**
     * 创建ERP 产品利润
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductProfit(@Valid ProductProfitSaveReqVO createReqVO);

    /**
     * 更新ERP 产品利润
     *
     * @param updateReqVO 更新信息
     */
    void updateProductProfit(@Valid ProductProfitSaveReqVO updateReqVO);

    /**
     * 删除ERP 产品利润
     *
     * @param id 编号
     */
    void deleteProductProfit(Long id);

    /**
     * 获得ERP 产品利润
     *
     * @param id 编号
     * @return ERP 产品利润
     */
    ProductProfitDO getProductProfit(Long id);

    /**
     * 获得ERP 产品利润分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 产品利润分页
     */
    PageResult<ProductProfitDO> getProductProfitPage(ProductProfitPageReqVO pageReqVO);

    /**
     * 根据产品id查询产品利润信息
     * @param productIds 产品id集合
     * @return 变种skc列表
     */
    List<ProductProfitDO> getProfitListByProductIds(Collection<Long> productIds);

}