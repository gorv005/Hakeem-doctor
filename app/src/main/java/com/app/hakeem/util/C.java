package com.app.hakeem.util;

import android.os.Environment;

/**
 * Created by aditya.singh on 1/23/2018.
 */

public interface C {

    int FRAGMENT_SPLASH = 1;
    int FRAGMENT_LOGIN = 2;
    int FRAGMENT_DOCTOR_REGISTRATION_STEP1 = 31;
    int FRAGMENT_DOCTOR_REGISTRATION_STEP2 = 32;
    int FRAGMENT_DOCTOR_REGISTRATION_STEP3 = 33;
    int FRAGMENT_DOCTOR_REGISTRATION_STEP4 = 34;
    int FRAGMENT_PATIENT_REGISTRATION_STEP1 = 41;
    int FRAGMENT_PATIENT_REGISTRATION_STEP2 = 42;
    int FRAGMENT_REGISTRATION_TYPE = 5;

    String TAG_FRAGMENT_LOGIN = "fragment_login";
    String TAG_FRAGMENT_REGISTER_P_1 = "TAG_FRAGMENT_REGISTER_P_1";
    String TAG_FRAGMENT_REGISTER_P_2 = "TAG_FRAGMENT_REGISTER_P_2";
    String TAG_FRAGMENT_SPLASH = "fragment_splash";

    String BUNDLE = "bundle";
    String LOGIN_USER = "login_user";
    String FRAGMENT_ACTION = "action";
    long SPLASH_LOADER_TIME = 100;
    String STATUS_SUCCESS = "HK001";

    String STATUS_NOT_KNOWN = "HK002";
    String STATUS_FAIL = "HK002";
    String IS_LOGIN = "is_login";

    String AUTH_TOKEN = "authToken";
    String PHOTO = "photo";
    String TEXT = "text";


    String VIDEO = "video";
    int API_TIME_OUT = 12000;
    String BASE_URL = "http://www.dataheadstudio.com/test/api";
    String API_POSTS = BASE_URL + "/posts";
    String API_LOGIN = BASE_URL + "/login";
    String API_UPLOAD_PIC = BASE_URL + "/upload";
    String API_REGISTER_PATIENT = BASE_URL + "/registerpatient";



    String TAG_FRAGMENT_REGISTER_TYPE = "RegisterType";
    String USER = "user";
    String DATE_FORMAT = "dd/MMM/yyyy";
    String IMAGE_PATH = Environment
            .getExternalStorageDirectory().getPath() + "/Hakeem";
    String TAG_FRAGMENT_REGISTER_TYPE_R_3 = "TAG_FRAGMENT_REGISTER_TYPE_R_3";
    String TAG_FRAGMENT_REGISTER_TYPE_R_4 = "TAG_FRAGMENT_REGISTER_TYPE_R_4";
    String USER_PATIENT = "3";
    String USER_DOCTOR = "2";
    String USER_ADMIN = "1";
}
