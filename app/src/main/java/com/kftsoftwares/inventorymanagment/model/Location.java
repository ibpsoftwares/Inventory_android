package com.kftsoftwares.inventorymanagment.model;

import com.kftsoftwares.inventorymanagment.model.Categories;

import java.util.ArrayList;

public class Location {

  private   String lid ,location, parent , Child_String;
    private ArrayList<Categories> children = null ;

    public String getChild_String() {
        return Child_String;
    }

    public void setChild_String(String child_String) {
        Child_String = child_String;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public ArrayList<Categories> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Categories> children) {
        this.children = children;
    }
}
