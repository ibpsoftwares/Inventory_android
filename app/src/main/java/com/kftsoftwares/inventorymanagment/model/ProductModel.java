package com.kftsoftwares.inventorymanagment.model;

import java.io.Serializable;

public class ProductModel implements Serializable {

    private  String productID,categoryID,main_cat,name,product_code,description,department,assigned_name,
            product_quantity,location_id,assigned_id,buynow_price,added_by,make,model,team_leader ,location_name,
            image,image2,image3,status,invoice_image,additional_info,invoice_date,putin_date,trash_reason;

    private boolean valueSet = false;

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public boolean isValueSet() {
        return valueSet;
    }

    public void setValueSet(boolean valueSet) {
        this.valueSet = valueSet;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getMain_cat() {
        return main_cat;
    }

    public void setMain_cat(String main_cat) {
        this.main_cat = main_cat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAssigned_name() {
        return assigned_name;
    }

    public void setAssigned_name(String assigned_name) {
        this.assigned_name = assigned_name;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getAssigned_id() {
        return assigned_id;
    }

    public void setAssigned_id(String assigned_id) {
        this.assigned_id = assigned_id;
    }

    public String getBuynow_price() {
        return buynow_price;
    }

    public void setBuynow_price(String buynow_price) {
        this.buynow_price = buynow_price;
    }

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTeam_leader() {
        return team_leader;
    }

    public void setTeam_leader(String team_leader) {
        this.team_leader = team_leader;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInvoice_image() {
        return invoice_image;
    }

    public void setInvoice_image(String invoice_image) {
        this.invoice_image = invoice_image;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }

    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
    }

    public String getPutin_date() {
        return putin_date;
    }

    public void setPutin_date(String putin_date) {
        this.putin_date = putin_date;
    }

    public String getTrash_reason() {
        return trash_reason;
    }

    public void setTrash_reason(String trash_reason) {
        this.trash_reason = trash_reason;
    }
}
