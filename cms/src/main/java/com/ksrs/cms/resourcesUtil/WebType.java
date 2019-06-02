package com.ksrs.cms.resourcesUtil;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebType implements Serializable {

    private String webTypeName;

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWebTypeName() {
        return webTypeName;
    }

    public void setWebTypeName(String webTypeName) {
        this.webTypeName = webTypeName;
    }
}
