package com.app.hakeem.util;

/**
 * Created by aditya.singh on 1/23/2018.
 */

public interface C {

    int FRAGMENT_SPLASH =1;
    int FRAGMENT_LOGIN = 2 ;
    int FRAGMENT_DOCTOR_REGISTRATION = 3;
    int FRAGMENT_PATIENT_REGISTRATION = 4;
    int FRAGMENT_REGISTRATION_TYPE = 5;

    String TAG_FRAGMENT_LOGIN = "fragment_login";
    String TAG_FRAGMENT_SPLASH ="fragment_splash" ;
    String BUNDLE = "bundle";

    String LOGIN_USER = "login_user";
    String FRAGMENT_ACTION ="action" ;
    long SPLASH_LOADER_TIME = 2500;
    String STATUS_SUCCESS = "HK001";
    String STATUS_NOT_KNOWN ="HK002" ;

    String STATUS_FAIL = "HK002" ;
    String IS_LOGIN = "is_login";
    String AUTH_TOKEN = "authToken";

    String PHOTO = "photo";
    String TEXT = "text";
    String VIDEO = "video";


    int API_TIME_OUT = 12000 ;
    String BASE_URL = "http://www.dataheadstudio.com/test/api";
    String API_POSTS = BASE_URL +"/posts" ;
    String API_LOGIN = BASE_URL+"/login";
}
