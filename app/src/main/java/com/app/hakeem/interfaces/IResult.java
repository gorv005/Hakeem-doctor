package com.app.hakeem.interfaces;




import org.json.JSONObject;

/**
 * Created by aditya.singh on 4/22/2017.
 */

public interface IResult {
    public void notifySuccess(String requestType, JSONObject response);
    public void notifyError(String requestType, String error);
}