package com.seasoning.util;

import javax.servlet.http.HttpServletRequest;

import com.seasoning.base.BaseMsg;
import com.seasoning.base.MsgConstant;
import org.apache.commons.lang3.StringUtils;

/**
 * 系统工具类
 * @author zhubo
 * @since 2016年11月23日
 */
public class CommonUtil {
	
	/**
	 * 请求返回方法
	 * @param msgConstant 返回码枚举对象
	 * @return 返回通用数据格式
	 */
	public static BaseMsg returnMsg(MsgConstant msgConstant){
		BaseMsg msg = new BaseMsg();
		msg.setCode(msgConstant.getCode());
		msg.setMsg(msgConstant.getDesc());
		msg.setData("");
		return msg;
	}
	/**
	 * 请求返回方法
	 * @param msgConstant 返回码枚举对象
	 * @param data 需要接口返回的数据
	 * @return 返回通用数据格式
	 */
	public static BaseMsg returnMsg(MsgConstant msgConstant, Object data){
		BaseMsg msg = new BaseMsg();
		msg.setCode(msgConstant.getCode());
		msg.setData(data);
		msg.setMsg(msgConstant.getDesc());
		return msg;
	}

	/**
	 * 通过HttpServletRequest返回IP地址
	 * @param request HttpServletRequest
	 * @return ip String
	 * @throws Exception 所有异常都在调用出捕获
	 */
	public static String getIpAddr(HttpServletRequest request) throws Exception {
	    String ip = request.getHeader("x-forwarded-for");
	    if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("HTTP_CLIENT_IP");
	    }
	    if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	    }
	    if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getRemoteAddr();
	    }
	    return ip;
	}
}
