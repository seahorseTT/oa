package com.ckt.oa.entity;


public class Department {
    private String sn;     //部门编号
    private String name;    //部门名称
    private String address; //部门地址

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
