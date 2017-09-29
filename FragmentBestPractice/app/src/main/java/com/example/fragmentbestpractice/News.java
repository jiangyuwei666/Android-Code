package com.example.fragmentbestpractice;

/**
 * Created by Administrator on 2017/7/28.
 */

public class News {

    private String title ;
    private String content ;

    public News ( String title , String content ) {
        this.title = title ;
        this.content = content ;
    }

    public String getTitle ( ){
        return this.title ;
    }

    public String getContent ( ) {
        return this.content ;
    }
}
