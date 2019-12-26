package com.seasoning.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.seasoning.base.BaseMsg;
import com.seasoning.base.CommonConstant;
import com.seasoning.base.MsgConstant;
import com.seasoning.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 系统工具类
 * @author zhubo
 * @since 2016年11月23日
 */
@Slf4j
public class CommonUtil {

	public static void updateResponse(MsgConstant msgConstant, HttpServletResponse response, Exception ex) throws IOException {
		BaseMsg msg = new BaseMsg();
		msg.setCode(msgConstant.getCode());
		msg.setMsg(msgConstant.getDesc());
		if(null == ex){
			msg.setData("");
		}else{
			msg.setData(ex.getMessage());
		}
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(JSON.toJSONString(msg));
	}
	
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
	public static <T> BaseMsg<T> returnMsg(MsgConstant msgConstant, T data){
		BaseMsg<T> msg = new BaseMsg();
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

	public static User getUserBySession(HttpSession session){
		Object obj = session.getAttribute(CommonConstant.USER_INFO);
		if(obj == null){
			return null;
		}
		if(obj instanceof User){
			return (User)obj;
		}
		log.error("session data error : {}", JSONObject.toJSONString(obj));
		return null;
	}

}
