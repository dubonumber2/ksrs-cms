package com.ksrs.cms.dto;

import java.io.Serializable;

public class LoginDto implements Serializable {
    private Object token;

    public Object getToken() {
        return token;
    }

    public void setToken(Object token) {
        this.token = token;
    }
}
