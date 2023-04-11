package com.yesol.manddang_1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yesol.manddang_1.repository.AreaRepository;
import com.yesol.manddang_1.vo.Area;
import com.yesol.manddang_1.vo.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaService {
    private final AreaRepository areaRepository;
    private static final Logger logger = LoggerFactory.getLogger(AreaService.class);

    @Transactional
    public String save(final Area area, HttpSession sess) {
        String result = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            Area getArea = areaRepository.save(area);
            result = mapper.writeValueAsString(getArea);

        } catch(Exception e){
            logger.error("AreaService.save ERROR :{}",e);
        }
        return result;
    }

    public List findAll() {
        String result = null;
        ObjectMapper mapper = new ObjectMapper();
        List list = new ArrayList();
        try {
            list = areaRepository.findAll();
            System.out.println(list.toString());

        } catch(Exception e){
            logger.error("AreaService.findAll ERROR :{}",e);
        }
        return list;
    }

    public String findSidoCdBySidoNm(String sido_nm, String area_nm) {
        String code = null;
        try {
            //step1 - sido_nm으로 sido_cd 찾아오기
            //step2 -
            //      조건 1 - area_nm이 전체면 흘려보내기
            //          2 - area_nm이 이름이 있으면 sido_cd 필터 걸어서 DB에서 area_cd 찾아오기
            System.out.println("###입력sido:"+sido_nm);
            System.out.println("###입력area:"+area_nm);
            code = areaRepository.selectSidoCdBySidoNm(sido_nm); //-> 여기에서 쿼리 넣어줘야 함
            //todo : area_nm 전체가 있는 값으로 인식이 안되는듯 경기 전체를 눌렀을 때 change 로 인식되지 않음 프론트단에서
            if(!area_nm.equals("전체")){
                code = areaRepository.selectAreaCdByAreaNm(code,area_nm);
            }

            System.out.println("###검색결과코드:"+code);

        } catch(Exception e){
            logger.error("AreaService.findSidoCdBySidoNm ERROR :{}",e);
        }
        return code;
    }
}
