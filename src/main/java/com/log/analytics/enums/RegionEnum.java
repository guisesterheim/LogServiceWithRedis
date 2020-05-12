package com.log.analytics.enums;

import java.util.Arrays;

public enum RegionEnum {

    NONE(0), USEAST1(1), USWEST2(2), APSOUTH1(3), ALL(4);

    private Integer id;

    private RegionEnum(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static RegionEnum valueOfId(Integer id) {
        return Arrays.stream(RegionEnum.values())
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}