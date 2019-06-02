package com.ksrs.cms.dto;

import java.io.Serializable;

public class PageChildDto<T> implements Serializable {

    private Integer total;

    private T list;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public T getList() {
        return list;
    }

    public void setList(T list) {
        this.list = list;
    }
}
