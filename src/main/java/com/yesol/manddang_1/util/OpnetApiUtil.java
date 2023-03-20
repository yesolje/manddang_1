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
    
    // 오피넷의 데이터에서 브랜드 코드를 브랜드 명으로 바꿔준다
    // 매개변수 - string (오피넷 데이터)
    // 산출물 - string
    public String replaceBrandCdToName(String apiData){

        String splite1 = apiData.substring(0,67);
        String splite2 = apiData.substring(67);
        //enum 순회하면서... 브랜드 네임으로 바꿀 수 있는 함수로 만들기
        BrandCode brandCodeList[] = BrandCode.values();
        for(BrandCode b: brandCodeList){
            splite2 = splite2.replaceAll(b.toString(),b.getBrandName());
        }
        apiData = splite1 + splite2;

        return apiData;
    }
    
}
