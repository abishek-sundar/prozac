package com.abisheksundar.prozac;

public class PhoneList {

    // fields


    private String name;
    private String phoneNum;

    // constructors

    public PhoneList() {}

    public PhoneList(String phoneNum_, String name_) {
        this.name = name_;
        this.phoneNum=phoneNum_;
    }

    // properties

    public void setName (String name_) {

        this.name = name_;

    }

    public void setPhoneNum (String phoneNum_) {

        this.phoneNum = phoneNum_;

    }

    public String getName() {

        return this.name;

    }

    public String getPhoneNum() {

        return this.phoneNum;

    }

}