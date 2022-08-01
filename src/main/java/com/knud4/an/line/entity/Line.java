package com.knud4.an.line.entity;

import com.knud4.an.Base.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Line extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @Builder
    public Line(String name) {
        this.name = name;
    }

    public void changeName(String name) {
        this.name = name;
    }
}