package com.RAC;

/**
 * Created with IntelliJ IDEA.
 * User: nick
 * Date: 3/25/14
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class Contact {
    String name;
    String phoneNumber;

    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
