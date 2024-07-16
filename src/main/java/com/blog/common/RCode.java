package com.blog.common;

/**
 * @Author: zwj
 * @Description: TODO
 * @DateTime: 2023/7/7 16:18
 **/
public enum RCode {
    UNKNOW(0,"请稍后重试"),
    SUCCESS(200,"请求成功"),
    ERROR(300,"请求失败"),
    PRODUCT_OFFLINE(301,"查询不到产品");
    //应答码 成功200 失败201
    private int code;
    private String text;

    RCode(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
