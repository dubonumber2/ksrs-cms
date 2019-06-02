package com.ksrs.cms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

public class RolePageDto<T> implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer total;

    private T roles;



    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public T getRoles() {
        return roles;
    }

    public void setRoles(T roles) {
        this.roles = roles;
    }
}
