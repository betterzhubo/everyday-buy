package com.seasoning.base;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口公共返回对象
 * @author zhubo
 * @since 2016年11月18日
 */
@Data
public class BaseMsg implements Serializable {
	/** 返回code码 */
	private int code;
	/** 返回提示信息 */
	private String msg;
	/** 程序返回数据 */
	private Object data;

}
