package com.yesol.manddang_1.util;

import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpnetApiUtil {

    // 오피넷과 통신하여 데이터를 받아온다
    // 매개변수 - url 주소
    // 산출물 - string
    public String ApiDataCall(URL url) throws IOException, ParseException {
        StringBuffer result = new StringBuffer();
        String strResult = "";

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");

        // 응답 데이터 받아오기
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 & conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        String line;
        while((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        conn.disconnect();
        strResult = result.toString();

        return strResult;
    }
}
