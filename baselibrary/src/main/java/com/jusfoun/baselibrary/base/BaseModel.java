package com.jusfoun.baselibrary.base;

import java.io.Serializable;

/**
 * Created by wang on 2016/11/8.
 */

public abstract class BaseModel implements Serializable {
    protected int code;

    protected int result;

    protected String msg;

    protected String error;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
