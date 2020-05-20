package com.seasoning.base;

/**
 * 该对象存储程序全部的返回code码和错误描述
 * @author zhubo
 * @since 2016年11月18日
 */
public enum MsgConstant {
	/** 0, 成功 */
	OK(0, "成功"),
	
	/** --------通用错误码-------- */
	/** 10001, 参数错误！ */
	PARAM_ERROR(10001, "参数错误！"),
	/** 10002, 参数为空！ */
	PARAM_IS_NULL(10002, "参数为空！"),
	/** 10003, 你还没有这个权限！ */
	CANNOT_OPERATION(10003, "您还没有这个权限！"),
	/** 10004, 手机号错误！ */
	PHONE_ERROR(10004, "手机号错误！"),
	/** 10005, 数据不存在！ */
	DATA_ERROR(10005, "数据不存在！"),

	/** 10006,特殊字符传入 */
	CHAR_FILTER(10006,"请勿填写特殊字符！"),

	/** 10007,调用第三方请求错误 */
	HTTP_REQUEST_ERROR(10007,"调用第三方请求错误！"),

	/** 10008, 短信验证码错误！ */
	SMS_VERIFICATION_CODE_ERROR(10008, "短信验证码错误！"),
	/** 10009, 短信验证码已过期，请重新获取！ */
	SMS_VERIFICATION_CODE_BE_OVERDUE(10009, "短信验证码已过期，请重新获取"),
	/** 10010, 短信验证码发送失败，请重试！ */
	SMS_VERIFICATION_CODE_SEND_ERROR(10010, "短信验证码发送失败，请重试！"),
	
	/** --------用户错误码-------- */
	/** 11001, 用户未登陆 */
	NOT_LOGIN(11001, "用户未登陆"),
	/** 11002, 用户已存在 */
	USER_EXIST(11002, "账号已存在"),
	/** 11003, 用户不存在 */
	USER_NO_EXIST(11003, "账号不存在"),
	/** 11004, 密码错误！ */
	PASSWORD_ERROR(11004, "密码错误！"),

	/** 11007, 登录已过期，请重新登录！ */
	LOGIN_OVERTIME(11007, "登录已过期，请重新登录！"),


	/** 13011, 订单状态错误 */
	ORDER_STATUS(13011, "订单状态错误！"),
	/** 13012, 订单支付失败！ */
	PAY_FAIL(13012, "订单支付失败！"),
	SHOPPINGCART_NULL(13013, "购物车是空的！"),



	/** --------程序异常错误码-------- */
	/** 404, 请求不存在 */
	NO_PAGE_ERROR(404, "请求不存在"),
	/** 90001, 程序异常！ */
	PROGRAM_ERROR(90001, "程序异常！"),
	/** 90002, 不支持的数据格式！！ */
	MEDIA_TYPE_ERROR(90002, "不支持的数据格式！"),
	/** 90003, 不支持的参数类型！ */
	DATA_TYPE_ERROR(90003, "不支持的参数类型！"),
	/** 90004, 不支持的请求类型！ */
	REQUEST_TYPE_ERROR(90004, "不支持的请求类型！"),
	/** 90005, 没有传参 */
	PARAM_NULL_ERROR(90005, "没有传参"),
	/** 90006, json解析异常！ */
	JSON_ERROR(90006, "json解析异常！"),
	/** 90007, sql语句异常！ */
	SQL_ERROR(90007, "数据库操作异常！"),
	/** 90008, 文件（上传）不存在！ */
	FILE_ERROR(90008, "文件不存在！"),
	/** 90009, 文件上传异常！ */
	FILE_GROUPIMG_ERROR(90009, "文件上传异常！"),
	/** 90010, 文件上传异常！ */
	FILE_UPLODE_ERROR(90010, "文件上传异常！")
	;
	
	/** 错误码 */
	private int code;
	/** 错误描述 */
	private String desc;
	
	private MsgConstant(int code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public int getCode(){
		return this.code;
	}
	
	public String getDesc(){
		return this.desc;
	}
}
