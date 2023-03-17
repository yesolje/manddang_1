package com.yesol.manddang_1.controller;

import com.yesol.manddang_1.util.OpnetApiUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.util.HashMap;

//지역 등록 겸 테스트를 위한 임시 컨트롤러 페이지
@RestController
@RequiredArgsConstructor
public class OilPriceController {

    //유가정보 조회
    @RequestMapping(value = "/search/getOilPrice", produces ="application/json")
    public ResponseEntity<?> getOilPrice(@RequestBody Object filter) {
        OpnetApiUtil op = new OpnetApiUtil();
        String strResult = "";
        String code="F211229279";
        HashMap<String,String> filterMap = (HashMap)filter;
        String sido_cd = (String)filterMap.get("sido_cd");
        String prod_cd = (String)filterMap.get("prod_cd");

        if(sido_cd ==null){
            sido_cd="01";
        }else{
            //TODO:지역필터 걸었을 경우 지역명에 따라 DB 정보에서 코드 찾아오는 작업 필요함
        }

        try {
            // URL 설정
            StringBuilder urlBuilder = new StringBuilder("http://www.opinet.co.kr/api/lowTop10.do");

            // search 변수는 인코딩이 필요하다고 했으므로 그 부분만 인코딩
            urlBuilder.append("?out=json");
            urlBuilder.append("&code=" + code);
            urlBuilder.append("&prodcd="+prod_cd);
            urlBuilder.append("&area="+sido_cd);
            urlBuilder.append("&cnt=20");

            URL url = new URL(urlBuilder.toString());

            strResult = op.ApiDataCall(url);

        } catch ( Exception e ){
            e.printStackTrace();
        }
        ResponseEntity res = new ResponseEntity<>(strResult, HttpStatus.OK);
        return res;
    }


    
}
