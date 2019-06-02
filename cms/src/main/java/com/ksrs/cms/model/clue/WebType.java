package com.ksrs.cms.model.clue;

import java.io.Serializable;

public class WebType implements Serializable{

    private Integer id;

    private String webType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWebType() {
        return webType;
    }

    public void setWebType(String webType) {
        this.webType = webType;
    }
}
