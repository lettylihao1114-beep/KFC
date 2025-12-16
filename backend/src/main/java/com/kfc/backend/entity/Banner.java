package com.kfc.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("banner")
public class Banner {
    private Long id;
    private String image;
    private String url;
    private Integer status;

    // --- 手动 Getter/Setter ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}