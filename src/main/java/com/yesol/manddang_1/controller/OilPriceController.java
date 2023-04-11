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
    private final AreaController areaController;

    //유가정보 조회
    @RequestMapping(value = "/search/getOilPrice", produces ="application/json")
    public ResponseEntity<?> getOilPrice(@RequestBody Object filter) {
        OpnetApiUtil op = new OpnetApiUtil();
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
                //TODO:지역필터 걸었을 경우 지역명에 따라 DB 정보에서 코드 찾아오는 작업 필요함

                String sido_cd = areaController.getSidoCdBySidoNm(sido_nm,area_nm);

                urlBuilder.append("?out=json");
                urlBuilder.append("&code=" + code);
                urlBuilder.append("&prodcd="+prod_cd);
                //urlBuilder.append("&area="+sido_cd);
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


    
}
