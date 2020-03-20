package com.seasoning.base;

import com.seasoning.util.TimeUtil;

import java.text.ParseException;

/**
 * @Description: 接口返回参数基本格式，时间不传为当前时间
 */
public class ResponseBaseModelT<T> {
    private Integer code;
    private String msg;
    private String time;
    private T data;

    public Integer getCode() {
        return code == null ? 0 : code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg==null?"操作成功":msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() throws ParseException {
        return time == null ? TimeUtil.getSystemTime() : time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResponseBaseModelT(){}

    public ResponseBaseModelT(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseBaseModelT(MsgConstant msgConstant) {
        this.code = msgConstant.getCode();
        this.msg = msgConstant.getDesc();
    }
}
