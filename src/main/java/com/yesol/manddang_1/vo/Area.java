package com.yesol.manddang_1.vo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name="AREA")
public class Area {

    @Id
    @Column(name = "AREA_CD")
    private String areaCd;

    @Column(name = "AREA_NM")
    private String areaNm;

    @Column(name = "SIDO_CD")
    private String sidoCd;

    @Column(name = "SIDO_NM")
    private String sidoNm;
}
