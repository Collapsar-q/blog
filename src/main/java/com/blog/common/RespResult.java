package com.blog.common;

import java.util.List;

public class RespResult {
    //应答码
    private int code;
    //code文字说明
    private String msg;
    //数据
    private Object data;
    private PageInfo pageInfo;
    private List list;
    private String accessToken;
    public RespResult() {
    }

    public RespResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public RespResult(int code, String msg, Object data, List list) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.list = list;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public RespResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public int getCode() {
        return code;
    }
    public static RespResult ok(String msg,Object data){
        RespResult respResult = new RespResult();
        respResult.setCode(200);
        respResult.setMsg(msg);
        respResult.setData(data);
        return respResult;
    }
    public static RespResult ok(int code,String msg,Object data,String accessToken){
        RespResult respResult = new RespResult();
        respResult.setCode(code);
        respResult.setMsg(msg);
        respResult.setData(data);
        respResult.setAccessToken(accessToken);
        return respResult;
    }
    public static RespResult ok(int code,String msg,Object data){
        RespResult respResult = new RespResult();
        respResult.setCode(code);
        respResult.setMsg(msg);
        respResult.setData(data);
        return respResult;
    }
    public static RespResult ok(int code,String msg){
        RespResult respResult = new RespResult();
        respResult.setCode(code);
        respResult.setMsg(msg);
        return respResult;
    }
    public static RespResult ok(String msg,Object data,PageInfo pageInfo){
        RespResult respResult = new RespResult();
        respResult.setCode(200);
        respResult.setMsg(msg);
        respResult.setData(data);
        respResult.setPageInfo(pageInfo);
        return respResult;
    }
    public static RespResult ok(String msg,Object data,List list){
        RespResult respResult = new RespResult();
        respResult.setCode(200);
        respResult.setMsg(msg);
        respResult.setData(data);
        respResult.setList(list);
        return respResult;
    }
    public static RespResult ok(Object data){
        RespResult respResult = new RespResult();
       respResult.setRCode(RCode.SUCCESS);
        respResult.setData(data);
        return respResult;
    }
    public void setRCode(RCode rCode){
        this.code=rCode.getCode();
        this.msg=rCode.getText();
    }
    public static RespResult error(RCode code){
        RespResult respResult = new RespResult();
        respResult.setRCode(code);
        return respResult;
    }
    public static RespResult error(){
        RespResult respResult = new RespResult();
       respResult.setRCode(RCode.UNKNOW);
        return respResult;
    }
    public static RespResult error(String msg){
        RespResult respResult = new RespResult();
        respResult.setRCode(RCode.ERROR);
        respResult.setMsg(msg);
        return respResult;
    }
    public static RespResult error(int code,String msg){
        RespResult respResult = new RespResult();
        respResult.setCode(code);
        respResult.setMsg(msg);
        return respResult;
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
}