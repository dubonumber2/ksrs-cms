package com.ksrs.cms.resourcesUtil;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Set;

public class ResourcesTree implements Serializable{

    private Integer id;

    private String name;
    @JsonProperty(value = "children")
    private Set<MenuTree> menuTrees;

    private String englishName;

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

    public Set<MenuTree> getMenuTrees() {
        return menuTrees;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public void setMenuTrees(Set<MenuTree> menuTrees) {
        this.menuTrees = menuTrees;
    }
}
