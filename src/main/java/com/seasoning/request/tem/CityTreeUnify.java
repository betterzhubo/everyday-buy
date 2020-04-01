package com.seasoning.request.tem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class CityTreeUnify {
    @ApiModelProperty(value = "市id")
    private Integer id;
    @ApiModelProperty(value = "市名称")
    private String lable;
    @ApiModelProperty(value = "等级")
    private Integer level;
    @ApiModelProperty(value = "区级列表")
    private List<AreaTree> children;
}
