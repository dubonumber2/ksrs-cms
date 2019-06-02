package com.ksrs.clue.resourcesUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Province implements Serializable{

    private Integer id;

    @JsonProperty(value = "value")
    private String provinceName;

    @JsonIgnore
    private Integer sort;
    @JsonProperty(value = "data")
    private Set<City> citys;



    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<City> getCitys() {
        return citys;
    }

    public void setCitys(Set<City> citys) {
        this.citys = citys;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

}
