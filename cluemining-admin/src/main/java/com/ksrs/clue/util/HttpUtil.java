package com.ksrs.clue.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtil {

    private static final String HTTP_PATH = "http://192.168.2.151:7070/us/user/getUserDownload";

    public static String getDowloadAble(String id) throws Exception {
        URL url = new URL(HTTP_PATH+"?id="+id);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization","aaaabbbbccc");
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        BufferedReader bReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line,resultStr="";

        while(null != (line=bReader.readLine()))
        {
            resultStr +=line;
        }
        bReader.close();
        return resultStr;
    }

    public static void main(String[] args) {
        try {
            String a = getDowloadAble("dc1d33693afc910e9218517d2ef21b3c");
            System.out.println(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
