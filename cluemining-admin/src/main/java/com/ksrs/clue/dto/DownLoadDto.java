package com.ksrs.clue.dto;

public class DownLoadDto {

    private String userId;

    private Integer downloadAble;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getDownloadAble() {
        return downloadAble;
    }

    public void setDownloadAble(Integer downloadAble) {
        this.downloadAble = downloadAble;
    }
}
