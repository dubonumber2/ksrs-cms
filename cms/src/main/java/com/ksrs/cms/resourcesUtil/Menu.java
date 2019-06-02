package com.ksrs.cms.resourcesUtil;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Menu implements Serializable{

    private Integer id;
    @NotNull(message = "资源名称不能为空")
    private String name;

    private String url;
    @NotNull(message = "父菜单ID不能为空")
    private Integer parentId;

    private String init;
    @NotNull(message = "资源类型不能为空")
    private Integer type;

    private String permission;

    private String englishName;

    private Integer sort;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        this.init = init;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    // 重写equals方法，自定义规则比较两个menu对象是否相等
    @Override
    public boolean equals(Object obj) {
        if(null == obj){
            return false;
        }
        if(this == obj){
            return true;
        }
        if(obj instanceof Menu){
            obj = (Menu)obj;
            return ((Menu) obj).getId().equals(this.getId());
        }
        return false;
    }
    // 重新hashconde方法
    @Override
    public int hashCode() {

        return id.hashCode()*name.hashCode();
    }
}
