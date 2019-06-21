package com.kangswx.rabbitmq.workqueue;


public class SMS {

    private String name;
    private String phone;
    private String content;

    public SMS() {
    }

    public SMS(String name, String phone, String content) {
        this.name = name;
        this.phone = phone;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
