package com.example.administrator;

/**
 * Created by Administrator on 2017/7/17.
 */

public class Person {

    private String name ;
    private  int ImageId ;
    private String shuxing ;

    public Person( String name , int ImageId , String shuxing ) {
        this.name = name ;
        this.ImageId = ImageId ;
        this.shuxing = shuxing ;
    }

    public String getName ( ) {
        return  this.name ;
    }

    public int getImageId ( ) {
        return this.ImageId ;
    }
}
