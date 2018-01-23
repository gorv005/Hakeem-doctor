package com.app.hakeem.pojo;

/**
 * Created by aditya.singh on 6/15/2016.
 */
public class SideMenuItem {

    private  int nameResourse;
    private  int imageNameResource;


    public SideMenuItem(int nameResourse, int imageNameResource) {
        this.nameResourse = nameResourse;
        this.imageNameResource = imageNameResource;
    }

    public int getNameResourse() {
        return nameResourse;
    }

    public void setNameResourse(int nameResourse) {
        this.nameResourse = nameResourse;
    }

    public int getImageNameResource() {
        return imageNameResource;
    }

    public void setImageNameResource(int imageNameResource) {
        this.imageNameResource = imageNameResource;
    }
}
