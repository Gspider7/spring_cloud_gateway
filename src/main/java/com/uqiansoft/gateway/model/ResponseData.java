package com.uqiansoft.gateway.model;

/**
 * Http请求返回数据封装
 * @author xutao
 * @date 2018-12-07 17:30
 */
public class ResponseData {

    /**
     * code定义
     */
    public static final String AUTHORITY_YES = "2";                 // 有访问权限
    public static final String AUTHORITY_NO = "900";                // 没有访问权限
    public static final String REQUEST_FAIL = "999";                // 请求失败


    private String code;

    private String msg;

    private String data;


    public ResponseData() {
    }

    public ResponseData(String code, String msg, String data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 构建没有权限的返回
     */
    public static ResponseData getNoAuthority() {
        return new ResponseData(AUTHORITY_NO, "没有访问权限", "");
    }

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
