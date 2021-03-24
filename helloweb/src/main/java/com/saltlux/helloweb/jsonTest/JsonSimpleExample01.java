package com.saltlux.helloweb.jsonTest;

import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonSimpleExample01 {
	 
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
 
        JSONObject obj = new JSONObject();
        obj.put("name", "tychejin.tistory.com");
        obj.put("age", 2019);
 
        JSONArray list = new JSONArray();
        list.add("messages01");
        list.add("messages02");
        list.add("messages03");
 
        obj.put("messages", list);
 
        try (FileWriter file = new FileWriter("c:\\Data.json")) {
            file.write(obj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        System.out.print(obj);
    }
}
