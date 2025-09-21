package com.video.common;

import java.util.HashMap;
import java.util.Map;

public class Result {
    //    静态属性
    private static final String SUCCESS = "0";
    private static final String ERROR = "-1";

    //    类的属性
    private String code;
    private String msg;
    private Object data;


    //    getter和setter方法
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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

    //    自定义方法
    public static Result success() {
//        创建了一个Result对象
        Result result = new Result();
        result.setCode(SUCCESS); // 把success的值设置为code的值
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
//      设置code 和数据
        result.setCode(SUCCESS);
        result.setData(data);
        return result;
    }

    public static Result success(Object data, long total) {
        Result result = new Result();
        result.setCode(SUCCESS);
        Map<String, Object> resultData = new HashMap<>();
        resultData.put("records", data);
        resultData.put("total", total);
        result.setData(resultData);
        return result;
    }

    public static Result error(String msg) {
        Result result = new Result();
//        设置code值和报错信息
        result.setCode(ERROR);
        result.setMsg(msg);
        return result;
    }
}
