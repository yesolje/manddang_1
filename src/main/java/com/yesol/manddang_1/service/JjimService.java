package com.yesol.manddang_1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yesol.manddang_1.repository.AreaRepository;
import com.yesol.manddang_1.repository.JjimRepository;
import com.yesol.manddang_1.vo.Area;
import com.yesol.manddang_1.vo.Jjim;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JjimService {

    private final JjimRepository jjimRepository;
    private static final Logger logger = LoggerFactory.getLogger(AreaService.class);

    @Transactional
    public String save(final Jjim jjim) {
        Date now = new Date();
        String result = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            jjim.setRegDate(now);
            Jjim postJjim = jjimRepository.save(jjim);
            result = mapper.writeValueAsString(postJjim);

        } catch(Exception e){
            logger.error("JjimService.save ERROR :{}",e);
        }
        return result;
    }
    
    //유저아이디로 찜한 주유소 목록 찾기
    public List getUniIdsByUserId(String user_name){
        List<Jjim> uniIdList = new ArrayList<>();
        try{
            uniIdList = jjimRepository.findByUserId(user_name);
        }catch(Exception e){
            logger.error("JjimService.getUniIdsByUserId ERROR :{}",e);
        }
        return uniIdList;
    }
}
