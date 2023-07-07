package com.yesol.manddang_1.controller;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.simple.JSONObject;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class GeoCodeController {

    @RequestMapping(value="/getLatLng", produces="application/json")//restcontroller 시에 필요 없음 produces는
    public ResponseEntity<?> getLatLng(@RequestBody Object filter){
        String apiKey = "AIzaSyDnWb62ygXw4GyLIT315CRB52qqLNT8NFI";
        HashMap<String,String> filterMap = (HashMap)filter;
        String address = filterMap.get("address");
        String strResult = "";

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();

        try {
            // 주소를 위도와 경도로 변환합니다.
            GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
            if (results.length > 0) {
                LatLng location = results[0].geometry.location;
                double latitude = location.lat;
                double longitude = location.lng;

                JSONObject jsonObject = new JSONObject();

                jsonObject.put("latitude", latitude);
                jsonObject.put("longitude", longitude);

                strResult = jsonObject.toJSONString();

                System.out.println("위도: " + latitude + ", 경도: " + longitude);
            } else {
                System.out.println("주소 변환 실패");
            }
        } catch (Exception e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
        ResponseEntity res = new ResponseEntity<>(strResult, HttpStatus.OK);
        return res;
    }
}
