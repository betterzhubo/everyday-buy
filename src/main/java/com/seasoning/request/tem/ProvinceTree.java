package com.seasoning.request.tem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class ProvinceTree {
    @ApiModelProperty(value = "省id")
    private Integer provinceid;
    @ApiModelProperty(value = "省名称")
    private String provincename;
    @ApiModelProperty(value = "等级")
    private Integer level;
    @ApiModelProperty(value = "市级列表")
    private List<CityTree> cityTreeList;
}
