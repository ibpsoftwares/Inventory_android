package com.kftsoftwares.inventorymanagment.util;

/**
 * Created by apple on 19/02/18.
 */

public class Constants {
    public static final String MyPREFERENCES = "MyPrefs" ;

    public static final String EMAIL = "email" ;
    public static final String ID = "id" ;
    public static final String NAME = "name" ;
    public static final String TYPE = "type" ;


    /**
     * "yyyy.MM.dd G 'at' HH:mm:ss z" ---- 2001.07.04 AD at 12:08:56 PDT
     "hh 'o''clock' a, zzzz" ----------- 12 o'clock PM, Pacific Daylight Time
     "EEE, d MMM yyyy HH:mm:ss Z"------- Wed, 4 Jul 2001 12:08:56 -0700
     "yyyy-MM-dd'T'HH:mm:ss.SSSZ"------- 2001-07-04T12:08:56.235-0700
     "yyMMddHHmmssZ"-------------------- 010704120856-0700
     "K:mm a, z" ----------------------- 0:08 PM, PDT
     "h:mm a" -------------------------- 12:08 PM
     "EEE, MMM d, ''yy" ---------------- Wed, Jul 4, '01
     */



    private static final String HOST_URL = "https://kft.co.in/inventory/restservice/";

    private static final String TOKEN = "MTUwS0ZUMTQ4S0ZUMjA5";


    public static final String LOGIN = HOST_URL+"login?token="+TOKEN;
    public static final String FORGET_PASSWORD = HOST_URL+"forgot_password?token="+TOKEN;
    public static final String GET_SELECTOR_DATA = HOST_URL+"get_select?token="+TOKEN;
    public static final String ADDPRODUCT = HOST_URL+"add_product?token="+TOKEN;
    public static final String EDIT_PRODUCT = HOST_URL+"edit_product?token="+TOKEN;
    public static final String VIEWPRODUCTS = HOST_URL+"get_products?token="+TOKEN;
    public static final String SEARCHPRODUCTS = HOST_URL+"filter?token="+TOKEN;
    public static final String DELETE_PRODUCT = HOST_URL+"delete_product?token="+TOKEN;
    public static final String VIEWACHIVE = HOST_URL+"archived_products?token="+TOKEN;
    public static final String VIEW_ARCHIVE_SEARCH = HOST_URL+"archived_filter?token="+TOKEN;
    public static final String RESTORE_PRODUCT = HOST_URL+"restore_product?token="+TOKEN;
    public static final String SEARCH_BY_RANGE = HOST_URL+"filterDateRange?token="+TOKEN;
    public static final String GET_ALL_CATEGORIES = HOST_URL+"getAllCategories?token="+TOKEN;
    public static final String GET_PRODUCT_BY_ID = HOST_URL+"getproductbyid?token="+TOKEN;

}

