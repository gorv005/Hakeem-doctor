package com.app.hakeem.pojo;

/**
 * Created by aditya.singh on 1/24/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityList {

    @SerializedName("LookUp")
    @Expose
    private Object lookUp;
    @SerializedName("Regions")
    @Expose
    private Object regions;
    @SerializedName("Cities")
    @Expose
    private List<City> cities = null;
    @SerializedName("Districts")
    @Expose
    private Object districts;
    @SerializedName("ZipCodes")
    @Expose
    private Object zipCodes;
    @SerializedName("ServiceCategories")
    @Expose
    private Object serviceCategories;
    @SerializedName("ServiceSubCategories")
    @Expose
    private Object serviceSubCategories;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private Object result;
    @SerializedName("statusdescription")
    @Expose
    private String statusdescription;
    @SerializedName("fullexception")
    @Expose
    private Object fullexception;

    /**
     * No args constructor for use in serialization
     *
     */
    public CityList() {
    }

    /**
     *
     * @param fullexception
     * @param result
     * @param serviceSubCategories
     * @param zipCodes
     * @param cities
     * @param districts
     * @param statusdescription
     * @param serviceCategories
     * @param lookUp
     * @param regions
     * @param success
     */
    public CityList(Object lookUp, Object regions, List<City> cities, Object districts, Object zipCodes, Object serviceCategories, Object serviceSubCategories, Boolean success, Object result, String statusdescription, Object fullexception) {
        super();
        this.lookUp = lookUp;
        this.regions = regions;
        this.cities = cities;
        this.districts = districts;
        this.zipCodes = zipCodes;
        this.serviceCategories = serviceCategories;
        this.serviceSubCategories = serviceSubCategories;
        this.success = success;
        this.result = result;
        this.statusdescription = statusdescription;
        this.fullexception = fullexception;
    }

    public Object getLookUp() {
        return lookUp;
    }

    public void setLookUp(Object lookUp) {
        this.lookUp = lookUp;
    }

    public Object getRegions() {
        return regions;
    }

    public void setRegions(Object regions) {
        this.regions = regions;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public Object getDistricts() {
        return districts;
    }

    public void setDistricts(Object districts) {
        this.districts = districts;
    }

    public Object getZipCodes() {
        return zipCodes;
    }

    public void setZipCodes(Object zipCodes) {
        this.zipCodes = zipCodes;
    }

    public Object getServiceCategories() {
        return serviceCategories;
    }

    public void setServiceCategories(Object serviceCategories) {
        this.serviceCategories = serviceCategories;
    }

    public Object getServiceSubCategories() {
        return serviceSubCategories;
    }

    public void setServiceSubCategories(Object serviceSubCategories) {
        this.serviceSubCategories = serviceSubCategories;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getStatusdescription() {
        return statusdescription;
    }

    public void setStatusdescription(String statusdescription) {
        this.statusdescription = statusdescription;
    }

    public Object getFullexception() {
        return fullexception;
    }

    public void setFullexception(Object fullexception) {
        this.fullexception = fullexception;
    }

}