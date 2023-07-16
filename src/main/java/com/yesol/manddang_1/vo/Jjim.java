package com.yesol.manddang_1.vo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@IdClass(JjimId.class)
@Table(name="JJIM")
public class Jjim {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Id
    @Column(name = "UNI_ID")
    private String uniId;

    @Column(name = "REG_DATE")
    private Date regDate;
}
