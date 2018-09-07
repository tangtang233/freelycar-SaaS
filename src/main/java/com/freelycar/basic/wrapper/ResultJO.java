package com.freelycar.basic.wrapper;

import com.freelycar.utils.ResultCode;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * ResultJO 返回前端的结果类
 * @author tangwei
 */
public class ResultJO {
    private int code;
    private String msg;
    private Object data;
    private HashMap<String, String> errorData;

    public ResultJO() {
    }

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

    public HashMap<String, String> getErrorData() {
        return errorData;
    }

    public void setErrorData(HashMap<String, String> errorData) {
        this.errorData = errorData;
    }

    public ResultJO addData(Object key, Object value) {
        if (this.data == null) {
            this.data = new HashMap();
        }

        if (this.data instanceof Map) {
            ((Map)this.data).put(key, value);
        }

        return this;
    }

    public static ResultJO getResultFromErrors(Errors errors) {
        ResultJO rj = new ResultJO();
        if (errors != null && errors.hasErrors()) {
            HashMap<String, String> errorMap = new HashMap<>();
            List<FieldError> errorlist = errors.getFieldErrors();
            Iterator var4 = errorlist.iterator();

            while(var4.hasNext()) {
                FieldError error = (FieldError)var4.next();
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            rj.setErrorData(errorMap);
            rj.setCode(ResultCode.UNKNOWN_ERROR.code());
        }

        return rj;
    }

    public static ResultJO getDefaultResult(Object data) {
        return getDefaultResult(data, "保存成功");
    }

    public static ResultJO getDefaultResult(Object data, String message) {
        ResultJO rj = new ResultJO();
        rj.setData(data);
        rj.setCode(ResultCode.SUCCESS.code());
        rj.setMsg(message);
        return rj;
    }

    public static ResultJO getErrorResult(Object data, String message) {
        ResultJO rj = new ResultJO();
        rj.setData(data);
        rj.setCode(ResultCode.UNKNOWN_ERROR.code());
        rj.setMsg(message);
        return rj;
    }

    public static ResultJO getErrorResult(Object data) {
        return getErrorResult(data, "保存出错");
    }

    public static ResultJO getNamedResult(HashMap map) {
        return getDefaultResult(map);
    }

    public static ResultJO getNamedResult(Object... data) {
        HashMap<String, Object> map = new HashMap<>();
        Object[] var2 = data;
        int var3 = data.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Object dataO = var2[var4];
            if (dataO != null) {
                map.put(dataO.getClass().getSimpleName().toLowerCase(), dataO);
            }
        }

        return getNamedResult(map);
    }

    public static <M> ResultJO getNamedResult(String name, M data) {
        HashMap<String, M> map = new HashMap<>(1);
        map.put(name, data);
        return getNamedResult(map);
    }
}
