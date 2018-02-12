package com.app.hakeem.pojo;

/**
 * Created by aditya.singh on 1/24/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Child {

    @SerializedName("id")
    @Expose
    private String childId;

    @SerializedName("user_id")
    @Expose
    private String parantId;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("relationship_id")
    @Expose
    private String relationshipID;

    @SerializedName("relation")
    @Expose
    private String relation;

    @SerializedName("relationPojo")
    @Expose
    private GeneralPojoKeyValue relationPojo;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    /**
     * No args constructor for use in serialization
     */
    public Child() {
    }


    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelationshipID() {
        return relationPojo.getKey();
    }

    public void setRelationshipID(String relationshipID) {
        this.relationshipID = relationshipID;
    }

    public GeneralPojoKeyValue getRelationPojo() {
        return relationPojo;
    }

    public void setRelationPojo(GeneralPojoKeyValue relationPojo) {
        setRelationshipID(relationPojo.getKey());
        this.relationPojo = relationPojo;
    }

    public String getParantId() {
        return parantId;
    }

    public void setParantId(String parantId) {
        this.parantId = parantId;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}