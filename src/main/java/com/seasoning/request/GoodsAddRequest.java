package com.seasoning.request;

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
public class GoodsAddRequest {

    @NotBlank(message = "参数不能为空")
    private String goodsName;

    @Range(min=0, max=1, message = "类型错误")
    private Integer purpose;

    @NotNull(message = "参数不能为空")
    @DecimalMin(value = "0")
    private BigDecimal price;

    @NotNull(message = "参数不能为空")
    private Integer goodsTypeId;
    @NotBlank(message = "参数不能为空")
    private String showImage;
    private String detailImage;

    private Boolean selling;
}
