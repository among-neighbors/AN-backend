package com.knud4.an.account.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {
    MALE, FEMALE;

    @JsonCreator
    public static Gender from(String s) {
        return Gender.valueOf(s.toUpperCase());
    }
}
