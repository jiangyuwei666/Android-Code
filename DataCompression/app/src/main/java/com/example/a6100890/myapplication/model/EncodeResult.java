package com.example.a6100890.myapplication.model;

import java.util.Map;

public class EncodeResult {
    //字符串编码后的二进制字符串
    private String encode;
    //字符串和二进制编码对
    private Map<Character, String> letterCode;

    public EncodeResult(String encode, Map<Character, String> letterCode) {
        this.encode = encode;
        this.letterCode = letterCode;
    }

    public String getEncode() {
        return encode;
    }

    public Map<Character, String> getLetterCode() {
        return letterCode;
    }
}
