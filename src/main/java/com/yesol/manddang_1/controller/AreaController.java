package com.yesol.manddang_1.controller;

import com.yesol.manddang_1.service.AreaService;
import com.yesol.manddang_1.vo.Area;
import com.yesol.manddang_1.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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



    
}
