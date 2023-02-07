package com.yesol.manddang_1.controller;

import com.yesol.manddang_1.service.AreaService;
import com.yesol.manddang_1.vo.Area;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequiredArgsConstructor
public class AreaController {

    private final AreaService AreaService;

    //지역코드등록(최초1번)
    @RequestMapping(value = "/manage/createArea", method = RequestMethod.POST)
    public ResponseEntity<?> createArea(@RequestBody Area area, HttpSession sess){
        String resultString = AreaService.save(area, sess);
        ResponseEntity res = new ResponseEntity<>(resultString, HttpStatus.CREATED);
        return res;
    }

    //지역코드호출(테스트용)
    @RequestMapping(value = "/manage/getArea", produces ="application/json")
    public ResponseEntity<?> getArea(@RequestBody String sido_cd) throws IOException {
        System.out.println("@@@@@@"+sido_cd);
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
        } catch ( Exception e ){
            e.printStackTrace();
        }
        ResponseEntity res = new ResponseEntity<>(strResult, HttpStatus.OK);
        return res;
    }


    
}
