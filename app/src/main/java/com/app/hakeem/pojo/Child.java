package com.app.hakeem.pojo;

/**
 * Created by aditya.singh on 1/24/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Child {

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
    private GeneralPojoKeyValue relation;


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
        return relation.getKey();
    }

    public void setRelationshipID(String relationshipID) {
        this.relationshipID = relationshipID;
    }

    public GeneralPojoKeyValue getRelation() {
        return relation;
    }

    public void setRelation(GeneralPojoKeyValue relation) {
        setRelationshipID(relation.getKey());
        this.relation = relation;
    }
}