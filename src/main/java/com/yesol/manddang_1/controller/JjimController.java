package com.yesol.manddang_1.controller;

import com.yesol.manddang_1.service.AreaService;
import com.yesol.manddang_1.service.JjimService;
import com.yesol.manddang_1.vo.Area;
import com.yesol.manddang_1.vo.Jjim;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class JjimController {
    private final JjimService JjimService;

    @RequestMapping(value = "/postStationId", produces ="application/json")
    public ResponseEntity<?> getOilPrice(@RequestBody Object data, Principal principal) {
        HashMap<String,String> dataMap = (HashMap)data;
        String uni_id = dataMap.get("uni_id");

        Jjim jjim = new Jjim();
        jjim.setUniId(uni_id);
        jjim.setUserId(principal.getName());

        String resultString = JjimService.save(jjim);
        ResponseEntity res = new ResponseEntity<>(resultString, HttpStatus.OK);

        return res;
    }
}
