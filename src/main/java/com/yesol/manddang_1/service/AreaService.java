package com.yesol.manddang_1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yesol.manddang_1.repository.AreaRepository;
import com.yesol.manddang_1.vo.Area;
import com.yesol.manddang_1.vo.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Date;

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

}
