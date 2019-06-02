package com.ksrs.cms.model.clue;

import java.io.Serializable;

public class Province implements Serializable{

    private Integer id;

    private String province;

    private boolean disabled=false;

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
