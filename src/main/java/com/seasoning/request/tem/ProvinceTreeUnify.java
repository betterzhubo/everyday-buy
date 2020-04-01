package com.seasoning.request.tem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class ProvinceTreeUnify {
    @ApiModelProperty(value = "省id")
    private Integer id;
    @ApiModelProperty(value = "省名称")
    private String lable;
    @ApiModelProperty(value = "等级")
    private Integer level;
    @ApiModelProperty(value = "市级列表")
    private List<CityTree> children;
}
