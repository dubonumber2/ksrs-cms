package com.ksrs.cms.dto;

import java.io.Serializable;

public class PageDto<T> implements Serializable {

    private Integer total;

    private T users;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public T getUsers() {
        return users;
    }

    public void setUsers(T users) {
        this.users = users;
    }
}
