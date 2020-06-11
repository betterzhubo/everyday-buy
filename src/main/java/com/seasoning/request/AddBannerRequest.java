package com.seasoning.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel
public class AddBannerRequest {

    @ApiModelProperty(value = "轮播图名称")
    @NotBlank(message = "轮播图名称不可以为空")
    private String bannerName;

    @ApiModelProperty(value = "轮播图排序值")
    @Range(min=0, max=Short.MAX_VALUE, message = "排序值错误")
    private Integer sort;

    @ApiModelProperty(value = "是否显示 0隐藏1显示")
    @Range(min=0, max=1, message = "是否显示错误")
    private Integer isShow;

    @ApiModelProperty(value = "轮播图地址")
    @NotBlank(message = "图片地址不为空")
    private String imageUrl;

    @ApiModelProperty(value = "轮播图点击跳转地址")
    @NotBlank(message = "跳转地址不为空")
    private String pageUrl;
}
