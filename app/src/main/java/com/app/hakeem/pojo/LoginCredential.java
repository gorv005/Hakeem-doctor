package com.app.hakeem.pojo;

/**
 * Created by aditya.singh on 1/24/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginCredential {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;

    /**
     * No args constructor for use in serialization
     *
     */
    public LoginCredential() {
    }

    /**
     *
     * @param email
     * @param password
     */
    public LoginCredential(String email, String password) {
        super();
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
