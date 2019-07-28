package com.seasoning.base;

/**
 * 接口公共返回对象
 * @author zhubo
 * @since 2016年11月18日
 */
public class BaseMsg {
	/** 返回code码 */
	private int code;
	/** 返回提示信息 */
	private String msg;
	/** 程序返回数据 */
	private Object data;
	/** 程序返回数据1 */
	private Object commondata;
	/** 程序返回版权数据1 */
	private Object copyright;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public Object getCommondata() {
		return commondata;
	}
	public void setCommondata(Object commondata) {
		this.commondata = commondata;
	}
	public Object getCopyright() {
		return copyright;
	}
	public void setCopyright(Object copyright) {
		this.copyright = copyright;
	}
}
