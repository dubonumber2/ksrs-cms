package com.ksrs.cms.dto;

import java.io.Serializable;

public class ResourcesDto<T> implements Serializable {

    private T resourcesIds;

    public T getResourcesIds() {
        return resourcesIds;
    }

    public void setResourcesIds(T resourcesIds) {
        this.resourcesIds = resourcesIds;
    }
}
