package com.saltlux.helloweb.jsonTest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
 
public class JsonSimpleExample02 {
    
    public static void main(String[] args) {
 
        JSONParser parser = new JSONParser();
 
        try (Reader reader = new FileReader("./json/data.json")) {
 
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            System.out.println(jsonObject);
 
            String name = (String) jsonObject.get("name");
            System.out.println(name);
 
            long age = (Long) jsonObject.get("age");
            System.out.println(age);
 
            // loop array
            JSONArray msg = (JSONArray) jsonObject.get("messages");
            Iterator<String> iterator = msg.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}