package com.yesol.manddang_1.repository;

import com.yesol.manddang_1.vo.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AreaRepository extends JpaRepository<Area,Long> {
    @Query(nativeQuery = true, value="SELECT a.sido_cd from manddang.area a where a.sido_nm = :sido_nm limit 1")
    String selectSidoCdBySidoNm(@Param("sido_nm") String sido_nm);

    @Query(nativeQuery = true, value="SELECT a.area_cd from manddang.area a where a.sido_cd = :sido_cd and a.area_nm = :area_nm")
    String selectAreaCdByAreaNm(@Param("sido_cd") String sido_cd , @Param("area_nm") String area_nm);
}
