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
    int FRAGMENT_DEPENDENT = 6;
    int FRAGMENT_EMR_AND_TRACKER = 7;
    int FRAGMENT_EMR = 71;
    int FRAGMENT_TRACKER = 72;
    int FRAGMENT_TRACKER_WEIGHT_REPORT = 721;
    int FRAGMENT_TRACKER_FEVER_REPORT = 722;
    int FRAGMENT_TRACKER_BLOOD_PRESSURE_REPORT = 723;
    int FRAGMENT_TRACKER_BLOOD_SUGAR_REPORT = 724;
    int FRAGMENT_PATIENT_EMR_AND_TRACKER = 8;
    int FRAGMENT_DOCTOR_PROFILE = 43;
    int FRAGMENT_DOCTOR_PATIENT_LIST = 44;
    int FRAGMENT_DOCTOR_PATIENT_DEPENDENT_LIST = 45;
    int FRAGMENT_PATIENT_PROFILE = 46;
    int FRAGMENT_COMMENTS = 47;
    int FRAGMENT_CONSULTATION_TYPE = 9;
    int FRAGMENT_CONSULTANT = 10;
    int FRAGMENT_SETTING = 11;
    int FRAGMENT_SELECT_PATIENT_TO_QUEUE = 12;
    int FRAGMENT_TERMS_AND_CONDITION =50 ;
    int FRAGMENT_OTP = 51;
    int FRAGMENT_DIAGNOSIS = 52;
    int FRAGMENT_PRESCRIPTION =53 ;
    int FRAGMENT_PATIENT_REGISTRATION_2 = 54;
    int FRAGMENT_SUPPORT =55 ;
    int FRAGMENT_DOCTOR_PROFILE_MANAGE =56 ;

    String TAG_FRAGMENT_LOGIN = "fragment_login";
    String TAG_FRAGMENT_REGISTER_P_1 = "TAG_FRAGMENT_REGISTER_P_1";
    String TAG_FRAGMENT_REGISTER_P_2 = "TAG_FRAGMENT_REGISTER_P_2";
    String TAG_FRAGMENT_SPLASH = "fragment_splash";
    String TAG_FRAGMENT_PATIENT_EMR_AND_TRACKER = "FRAGMENT_PATIENT_EMR_AND_TRACKER ";
    String TAG_FRAGMENT_EMR_AND_TRACKER = "EMR_AND_TRACKER ";
    String TAG_FRAGMENT_TRACKER_FEVER_REPORT = "TAG_FRAGMENT_TRACKER_FEVER_REPORT ";

    String TAG_FRAGMENT_TRACKER_WEIGHT_REPORT = "TAG_FRAGMENT_TRACKER_WEIGHT_REPORT ";
    String TAG_FRAGMENT_TRACKER_BLOOD_PRESSURE_REPORT = "TAG_FRAGMENT_TRACKER_BLOOD_PRESSURE_REPORT ";
    String TAG_FRAGMENT_TRACKER_BLOOD_SUGAR_REPORT = "TAG_FRAGMENT_TRACKER_BLOOD_SUGAR_REPORT ";

    String TAG_FRAGMENT_CONSULTATION_TYPE = "TAG_FRAGMENT_CONSULTATION_TYPE";
    String TAG_FRAGMENT_CONSULTANT = "TAG_FRAGMENT_CONSULTANT";
    String TAG_FRAGMENT_EMR = "EMR ";
    String TAG_FRAGMENT_TRACKER = "TRACKER ";
    String TAG_FRAGMENT_TRACKER_WEIGHT = "TAG_FRAGMENT_TRACKER_WEIGHT ";
    String TAG_FRAGMENT_TRACKER_FEVER = "TAG_FRAGMENT_TRACKER_FEVER ";
    String TAG_FRAGMENT_TRACKER_BLOOD_PRESSURE = "TAG_FRAGMENT_TRACKER_BLOOD_PRESSURE ";

    String TAG_FRAGMENT_DOCTOR_PROFILE = "TAG_FRAGMENT_DOCTOR_PROFILE";
    String TAG_FRAGMENT_COMMENTS = "TAG_FRAGMENT_COMMENTS";

    String TAG_FRAGMENT_PATIENT_PROFILE = "TAG_FRAGMENT_PATIENT_PROFILE";
    String TAG_FRAGMENT_DOCTOR_PATIENT_LIST = "TAG_FRAGMENT_DOCTOR_PATIENT_LIST";
    String TAG_FRAGMENT_DOCTOR_PATIENT_DEPENDENT_LIST = "TAG_FRAGMENT_DOCTOR_PATIENT_DEPENDENT_LIST";
    String TAG_FRAGMENT_TRACKER_BLOOD_SUGAR = "TAG_FRAGMENT_TRACKER_BLOOD_SUGAR";
    String TAG_FRAGMENT_SETTING ="TAG_FRAGMENT_SETTING" ;

    String BUNDLE = "bundle";
    String LOGIN_USER = "login_user";
    String FRAGMENT_ACTION = "action";
    long SPLASH_LOADER_TIME = 100;

    String STATUS_SUCCESS = "HK001";
    String STATUS_NOT_KNOWN = "HK002";
    String STATUS_FAIL = "HK002";
    String STATUS_CHAT_WITH_DOCTOR = "HK004";
    String STATUS_MAKE_PAYMENT = "HK005";


    String IS_LOGIN = "is_login";
    String AUTH_TOKEN = "authToken";
    String PHOTO = "photo";
    String TEXT = "text";
    String VIDEO = "video";

    int API_TIME_OUT = 12000;
    String BASE_URL = "http://www.dataheadstudio.com/test/api";
    String API_GET_OTP = BASE_URL + "/get_otp";
    String API_VERIFY_OTP = BASE_URL + "/verify_otp";

    String API_POSTS = BASE_URL + "/posts";
    String API_POSTS_LIKE = BASE_URL + "/postlike";
    String API_ADD_POSTS = BASE_URL + "/addpost";
    String API_LOGIN = BASE_URL + "/login";
    String API_DOC_REG = BASE_URL + "/registerdoctor";
    String API_UPLOAD_PIC = BASE_URL + "/upload";
    String API_REGISTER_PATIENT = BASE_URL + "/registerpatient";
    String API_REGISTER_FETCH_PATIENT = BASE_URL + "/fetchpatient";
    String API_REGISTER_FETCH_DOCTOR_PATIENTS = BASE_URL + "/patients";
    String API_GET_CHAT_GROUP = BASE_URL + "/getchatgroup";
    String API_GET_PRESCRIPTION = BASE_URL + "/getprescription";

    String API_GET_FEVER_REPORT = BASE_URL + "/getreportfever";
    String API_GET_WEIGHT_REPORT = BASE_URL + "/getreportweight";
    String API_GET_BLOOD_PRESSURE_REPORT = BASE_URL + "/getbloodpressure";
    String API_ADD_WEIGHT_REPORT = BASE_URL + "/reportweight";
    String API_ADD_BLOOD_SUGER_REPORT = BASE_URL + "/reportbloodsugar";

    String API_GET_COMMENTS = BASE_URL + "/comments";
    String ADD_COMMENTS = BASE_URL + "/usercomment";
    String API_ADD_FEVER_REPORT = BASE_URL + "/reportfever";
    String API_ADD_BLOOD_PRESSURE_REPORT = BASE_URL + "/reportbloodpressure";
    String API_GET_BLOOD_SUGER_REPORT = BASE_URL + "/getbloodsugar";
    String API_ADD_DPENDENT = BASE_URL + "/dependentadd";
    String API_AWARENESS = BASE_URL + "/awareness";
    String API_DELETE_DPENDENT = BASE_URL + "/dependentdelete";
    String API_CONSULTANT = BASE_URL + "/specialization";

    String API_PATIENT_QUEUE = BASE_URL + "/patientqueue";
    String API_ALL_QUEUE_PATIENT = BASE_URL + "/getallqueuepatient";


    String API_GO_ONLINE = BASE_URL + "/godoctor_online";
    String API_START_CHAT = BASE_URL + "/startchat";
    String API_END_CHAT = BASE_URL + "/endchat";
    String API_PRESCRIPTION = BASE_URL + "/prescription";
    String API_FOLLOWUP = BASE_URL + "/followup";
    String API_CHECK_FOLLOWUP = BASE_URL + "/checkfollowup";

    String API_REGISTER_TOKEN = BASE_URL + "/registration_ids";
    String API_MAKE_PAYMENT = BASE_URL + "/make_payment";
    String API_GET_DOC_DATA = BASE_URL+"/getdoctor";

    String TAG_FRAGMENT_REGISTER_TYPE = "RegisterType";
    String USER = "user";
    String DATE_FORMAT = "dd/MMM/yyyy";
    String DATE_FORMAT_FOR_REPORT = "yyyy-MM-dd";

    String IMAGE_PATH = Environment
            .getExternalStorageDirectory().getPath() + "/Hakeem";

    String TAG_FRAGMENT_REGISTER_TYPE_R_3 = "TAG_FRAGMENT_REGISTER_TYPE_R_3";

    String TAG_FRAGMENT_REGISTER_TYPE_R_4 = "TAG_FRAGMENT_REGISTER_TYPE_R_4";
    String TAG_FRAGMENT_DEPENDENT = "tag_fragment_dependent";
    String TAG_FRAGMENT_ADD_INQUEU ="TAG_FRAGMENT_ADD_INQUEU" ;
    String USER_PATIENT = "3";
    String API_SUPPORT = BASE_URL + "/support";
    String API_UPDATE_PROFILE_DOCTOR = BASE_URL + "/updatedoctor";


    String USER_DOCTOR = "2";
    String USER_ADMIN = "1";
    String DOCTOR = "Doctor";
    String PATIENT = "Patient";

    String DEPENDENT_ID = "dependent_id";
    String NAME = "name";
    String GENDER = "gender";
    String DOB = "dob";
    String FONT = "Mont.otf";
    String PATIENT_ID = "patientId";
    String PRE_MEAL = "Pre-meal";
    String POST_SLEEP = "Post-sleep";
    String AWARENESS_LIST = "awarenesslist";
    String SPECIALITY = "speciality";
    String SENDER = "SENDER";
    String RECEIVER = "RECEIVER";
    String POST = "post";
    String FALSE = "0";
    String TRUE = "1";

    String PASSWORD = "Admin@123";
    String IMAGE = "image";

    String CHAT_DOCTOR_ID = "chat_doc";
    String IS_DOCOTR_ONLINE = "is_docotr_online";
    String TOTAL_PERSON_INQUEUE = "TOTAL_PERSON_INQUEUE";
    String REGISTRATION_COMPLETE = "registrationComplete";
    String PUSH_NOTIFICATION = "pushNotification";
    // id to handle the notification in the notification tray
    int NOTIFICATION_ID = 100;
    int NOTIFICATION_ID_BIG_IMAGE = 101;

    String SHARED_PREF = "ah_firebase";
    String TOKEN = "tadddoffn";
    String CHAT_PATIENT_ID = "CHAT_PATIENT_ID";
    String NEW_PATIENT = "New Patient";
    String END_CHAT = "End Chat";
    String START_CHATTING = "Start Chating";
    String TITLE = "title";
    String DIAGNOSIS = "diagnosis";
    String FOLLOWUP = "followup";
    String PRESCRIPTION = "prescription";
    String IS_NOFICATION = "IS_NOFICATION";
    String PAYMENT_URL = "PAYMENT_URL";

    String TAG_FRAGMENT_TERMS_AND_CONDITION ="TAG_FRAGMENT_TERMS_AND_CONDITION" ;
    String TAG_FRAGMENT_OTP = "TAG_FRAGMENT_OTP";
    String DOCTOR_DETAILS = "doc_detail";
    String IS_DOC = "is_doc";
    String DETAILS = "details";
    String USER_ID = "user_id";

    String TAG_FRAGMENT_REGISTER_TYPE_R_2 ="TAG_FRAGMENT_REGISTER_TYPE_R_2" ;
    java.lang.String NUMBER_FORMAT ="966-" ;
    String English="en";
    String ARABIC="ar";
    String language="lan";
    String LANGUAGE ="language" ;
    String OBJECT ="object" ;
    String TAG_FRAGMENT_SUPPORT ="TAG_FRAGMENT_SUPPORT" ;
    String TAG_FRAGMENT_DOCTOR_PROFILE_MANAGE ="TAG_FRAGMENT_DOCTOR_PROFILE_MANAGE" ;
}
