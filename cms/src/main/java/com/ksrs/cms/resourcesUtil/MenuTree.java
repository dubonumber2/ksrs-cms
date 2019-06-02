package com.ksrs.cms.resourcesUtil;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Set;

public class MenuTree implements Serializable{

    private Integer id;
    private String name;
    private String url;
    private Integer parentId;
    private Integer type;
    private String englishName;
    @JsonProperty(value = "children")
    private Set<Menu> menuTrees;

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Set<Menu> getMenuTrees() {
        return menuTrees;
    }

    public void setMenuTrees(Set<Menu> menuTrees) {
        this.menuTrees = menuTrees;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
