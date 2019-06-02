package com.ksrs.clue.resourcesUtil;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourcesTree implements Serializable {

    private Integer id;

    private String name;

    private String init;

    private String url;

    private String englishName;

    private Integer type;

    private Set<MenuTree> menuTrees;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        this.init = init;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
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

    public Set<MenuTree> getMenuTrees() {
        return menuTrees;
    }

    public void setMenuTrees(Set<MenuTree> menuTrees) {
        this.menuTrees = menuTrees;
    }
}
