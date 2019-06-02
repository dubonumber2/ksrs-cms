package com.ksrs.clue.resourcesUtil;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class SelectTag implements Serializable{

    private Set<WebType> webType = new HashSet<>();

    private Set<Province> province = new HashSet<>();

    public Set<WebType> getWebType() {
        return webType;
    }

    public void setWebType(Set<WebType> webType) {
        this.webType = webType;
    }

    public Set<Province> getProvince() {
        return province;
    }

    public void setProvince(Set<Province> province) {
        this.province = province;
    }
}
