package com.yesol.manddang_1.controller;

import com.yesol.manddang_1.service.AreaService;
import com.yesol.manddang_1.vo.Area;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
//지역 등록 겸 테스트를 위한 임시 컨트롤러 페이지
@RestController
@RequiredArgsConstructor
public class AreaController {

    private final AreaService AreaService;

    //지역코드호출
    @RequestMapping(value = "/manage/getArea", produces ="application/json")
    public ResponseEntity<?> getArea() {
        List resultList = AreaService.findAll();
        ResponseEntity res = new ResponseEntity<>(resultList, HttpStatus.CREATED);
        return res;
    }

    //지역코드호출및 등록(테스트용)
    @RequestMapping(value = "/manage/createArea", produces ="application/json")
    public ResponseEntity<?> createArea(@RequestBody String sido_cd) throws IOException {
        StringBuffer result = new StringBuffer();
        String strResult = "";
        String code="F211229279";
        try {
            // URL 설정
            StringBuilder urlBuilder = new StringBuilder("http://www.opinet.co.kr/api/areaCode.do");

            // search 변수는 인코딩이 필요하다고 했으므로 그 부분만 인코딩
            urlBuilder.append("?out=json");
            urlBuilder.append("&code=" + code);
            urlBuilder.append("&area="+sido_cd);

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            // Request 형식 설정
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
            String result1 = strResult.replaceAll("\\s", "");
            String result2 = result1.replaceAll("\"", "\\\"");

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(result2);


            JSONObject jsonMain = (JSONObject)obj;
            Object obj2 =jsonMain.get("RESULT");

            JSONObject jsonResult = (JSONObject)obj2;
            Object obj3 =jsonResult.get("OIL");

            JSONArray jsonArr = (JSONArray)obj3;
            if (jsonArr.size() > 0){
                for(int i=0; i<jsonArr.size(); i++){
                    JSONObject jsonObj = (JSONObject)jsonArr.get(i);
                    Area area = new Area();
                    area.setAreaCd((String)jsonObj.get("AREA_CD"));
                    area.setAreaNm((String)jsonObj.get("AREA_NM"));
                    //하기를 켜놓으면 DB 저장 작업이 이루어짐
                    //String resultString = AreaService.save(area,null); 
                }
            }
        } catch ( Exception e ){
            e.printStackTrace();
        }
        ResponseEntity res = new ResponseEntity<>(strResult, HttpStatus.OK);
        return res;
    }


    
}
