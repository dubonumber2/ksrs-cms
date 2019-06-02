package com.ksrs.clue.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PageDto<T> {

    private Integer total;
    @JsonProperty(value = "list")
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
