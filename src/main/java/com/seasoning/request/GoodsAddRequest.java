package com.seasoning.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Authot:better
 * @Date:2019/8/1 11:22
 */
@Data
@ApiModel
public class GoodsAddRequest {


    @ApiModelProperty(value = "商品名称")
    @NotBlank(message = "参数不能为空")
    private String goodsName;

    @ApiModelProperty(value = "商品用途")
    @Range(min=0, max=1, message = "类型错误")
    private Integer purpose;

    @ApiModelProperty(value = "商品价格")
    @NotNull(message = "参数不能为空")
    @DecimalMin(value = "0")
    private BigDecimal price;

    @ApiModelProperty(value = "商品类型id")
    @NotNull(message = "参数不能为空")
    private Integer goodsTypeId;

    @ApiModelProperty(value = "展示图片地址")
    @NotBlank(message = "参数不能为空")
    private String showImage;

    @ApiModelProperty(value = "是否销售中")
    private Boolean selling;

    @ApiModelProperty(value = "是否热卖中")
    private Boolean isHot;
}
