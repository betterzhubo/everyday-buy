package com.seasoning.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel
public class UpdateBannerRequest {

    @ApiModelProperty(value = "轮播图id")
    private Integer id;

    @ApiModelProperty(value = "轮播图名称")
    private String bannerName;

    @ApiModelProperty(value = "轮播图排序值")
    private Integer sort;

    @ApiModelProperty(value = "是否显示 0隐藏1显示")
    private Integer isShow;

    @ApiModelProperty(value = "轮播图地址")
    private String imageUrl;

    @ApiModelProperty(value = "轮播图点击跳转地址")
    private String pageUrl;
}
