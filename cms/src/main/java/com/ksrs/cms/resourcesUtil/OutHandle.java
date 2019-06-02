package com.ksrs.cms.resourcesUtil;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OutHandle implements Serializable{

    String roleName;
    @JsonProperty(value = "children")
    Object permissions;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Object getPermissions() {
        return permissions;
    }

    public void setPermissions(Object permissions) {
        this.permissions = permissions;
    }
}
