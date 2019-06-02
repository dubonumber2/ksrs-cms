package com.ksrs.clue.resourcesUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class WebType implements Serializable {

    @JsonIgnore
    private Integer id;
    @JsonProperty(value = "value")
    private String webTypeName;

    private String paraName;

    public String getParaName() {
        return paraName;
    }

    public void setParaName(String paraName) {
        this.paraName = paraName;
    }

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
