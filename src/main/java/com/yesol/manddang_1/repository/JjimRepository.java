package com.yesol.manddang_1.repository;

import com.yesol.manddang_1.vo.Jjim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JjimRepository extends JpaRepository<Jjim,Long> {
    List<Jjim> findByUserId(String userId);
}
