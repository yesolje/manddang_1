package com.yesol.manddang_1.util;

public enum BrandCode {
    SKE("SK에너지"),
    GSC("GS칼텍스"),
    HDO("현대오일뱅크"),
    SOL("S-OIL"),
    RTO("자영알뜰"),
    RTX("고속도로알뜰"),
    NHO("농협알뜰"),
    ETC("자가상표"),
    E1G("E1"),
    SKG("SK가스");

    private final String brandName;

    BrandCode(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandName() {
        return brandName;
    }
}
