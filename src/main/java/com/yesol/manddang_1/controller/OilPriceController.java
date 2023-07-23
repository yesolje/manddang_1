package com.yesol.manddang_1.controller;

import com.yesol.manddang_1.service.JjimService;
import com.yesol.manddang_1.util.ApiUtil;
import com.yesol.manddang_1.vo.Jjim;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//이 컨트롤러는 오피넷의 API 와 통신하여 정보를 클라이언트로 전달합니다.
@RestController
@RequiredArgsConstructor
public class OilPriceController {
    private final AreaController areaController;
    private final GeoCodeController geoCodeController;
    private final JjimService jjimService;
    //유가정보 조회
    @RequestMapping(value = "/search/getOilPrice", produces ="application/json")
    public ResponseEntity<?> getOilPrice(@RequestBody Object filter) {
        ApiUtil op = new ApiUtil();
        String strResult = ""; //api 데이터 들어감
        String area =""; //db에서 찾아온 지역 코드 들어감
        String code="F211229279";
        HashMap<String,String> filterMap = (HashMap)filter;
        String sido_nm = filterMap.get("sido_nm");
        String area_nm = filterMap.get("area_nm");
        String prod_cd = filterMap.get("prod_cd");
        try {
            // URL 설정
            StringBuilder urlBuilder = new StringBuilder("http://www.opinet.co.kr/api/lowTop10.do");
            if(sido_nm.equals("전체")){
                urlBuilder.append("?out=json");
                urlBuilder.append("&code=" + code);
                urlBuilder.append("&prodcd="+prod_cd);
                urlBuilder.append("&cnt=20");
            }else{
                String sido_cd = areaController.getSidoCdBySidoNm(sido_nm,area_nm);

                urlBuilder.append("?out=json");
                urlBuilder.append("&code=" + code);
                urlBuilder.append("&prodcd="+prod_cd);
                urlBuilder.append("&area="+sido_cd);
                urlBuilder.append("&cnt=20");
            }

            URL url = new URL(urlBuilder.toString());

            strResult = op.ApiDataCall(url);
            strResult = op.replaceBrandCdToName(strResult);
        } catch ( Exception e ){
            e.printStackTrace();
        }
        ResponseEntity res = new ResponseEntity<>(strResult, HttpStatus.OK);
        return res;
    }

    //주유소 ID로 주유소 한건 정보 조회
    @RequestMapping(value="/getStationDetailInfo", produces="application/json")
    public ResponseEntity getStationDetailInfo(@RequestBody Object filter){
        ApiUtil op = new ApiUtil();
        String strResult = ""; //api 데이터 들어감
        String code="F211229279";
        HashMap<String,String> filterMap = (HashMap)filter;
        String id = filterMap.get("id");
        try {
            // URL 설정
            StringBuilder urlBuilder = new StringBuilder("http://www.opinet.co.kr/api/detailById.do");
            urlBuilder.append("?out=json");
            urlBuilder.append("&code=" + code);
            urlBuilder.append("&id="+id);
            URL url = new URL(urlBuilder.toString());
            strResult = op.ApiDataCall(url);
            strResult = op.replaceBrandCdToName(strResult);
        } catch ( Exception e ){
            e.printStackTrace();
        }
        ResponseEntity res = new ResponseEntity<>(strResult, HttpStatus.OK);
        return res;
    }

    //유저정보로 찜한 주유소의 정보 가져오기
    //유저가 찜한 uniId들을 불러와 api를 여러번 호출하여 각각의 주유소 상세정보들을 받은 후 리턴한다
    @RequestMapping(value="/getStationDetailInfosByUserId", produces ="application/json")
    public ResponseEntity getStationDetailInfosByUserId(Principal principal){
        String user_name = principal.getName();

        List<Jjim> jjimList = new ArrayList<>();
        jjimList = jjimService.getUniIdsByUserId(user_name);

        JSONArray jsonArr = new JSONArray();

        for(int i = 0 ; i<jjimList.size();i++){

            ///단건찾기 api 콜
            ApiUtil op = new ApiUtil();
            String strResult = ""; //api 데이터 들어감
            String code="F211229279";
            String id = jjimList.get(i).getUniId();
            try {
                // URL 설정
                StringBuilder urlBuilder = new StringBuilder("http://www.opinet.co.kr/api/detailById.do");
                urlBuilder.append("?out=json");
                urlBuilder.append("&code=" + code);
                urlBuilder.append("&id="+id);
                URL url = new URL(urlBuilder.toString());

                strResult = op.ApiDataCall(url);
                strResult = op.replaceBrandCdToName(strResult);

                JSONParser parser = new JSONParser();
                Object obj = parser.parse(strResult);

                JSONObject jsonMain = (JSONObject)obj;
                Object obj2 =jsonMain.get("RESULT");

                JSONObject jsonResult = (JSONObject)obj2;
                Object obj3 =jsonResult.get("OIL");

                JSONArray arr = (JSONArray)obj3;

                JSONObject jsonObj = (JSONObject)arr.get(0);

                jsonObj = geoCodeController.getLatLng(jsonObj);

                jsonArr.add(jsonObj);
            } catch ( Exception e ){
                e.printStackTrace();
            }
        }

        ResponseEntity res = new ResponseEntity<>(jsonArr, HttpStatus.OK);
        return res;
    }

}
