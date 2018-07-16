package com.konsonx.utils;

import com.konsonx.po.NavLocation;
import org.junit.jupiter.api.Test;

class YunTuTest {

    void insertByLocation() {
        YunTu yunTu = new YunTu();
        NavLocation location = new NavLocation();
        location.setName("淅水咖啡厅");
        location.setAddress("广东省广州市番禺区小谷围街道广东药科大学淅水咖啡厅");
        location.setAvailable(9);
        location.setLongitude("113.3968");
        location.setLatitude("23.061882");
        System.out.println(yunTu.insertByAddress(location));

    }


    void update() {
        YunTu yunTu = new YunTu();
        NavLocation location = new NavLocation();
        location.setId(4);
        location.setName("银天公寓");
        location.setAddress("广东省广州市番禺区小谷围街道都市丽人(大学城中二横路)");
        location.setAvailable(5);
        location.setLongitude("113.394668");
        location.setLatitude("23.060493");
        System.out.println(yunTu.update(location));
    }


    void deleteArray() {
        String[] ids = new String[]{"3","7"};
        YunTu yunTu = new YunTu();
        System.out.println(yunTu.delete(ids));
    }

    @Test
    void delete() {
        String[] ids = new String[]{"5"};
        YunTu yunTu = new YunTu();
        System.out.println(yunTu.delete(ids));
    }
}