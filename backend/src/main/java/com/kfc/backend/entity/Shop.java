package com.kfc.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("shop")
public class Shop {
    private Long id;
    private String name;
    private String address;
    private Integer status;
    private String openHours;
    private String image;

    // --- 手动 Getter/Setter ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getOpenHours() { return openHours; }
    public void setOpenHours(String openHours) { this.openHours = openHours; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}