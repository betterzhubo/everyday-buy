package com.seasoning.request;

import lombok.Data;

@Data
public class PageRequest {

    private Integer current;

    private Integer pageSize;
}
