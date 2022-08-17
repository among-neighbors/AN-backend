package com.knud4.an.board;

import com.knud4.an.Base.BaseEntity;
import com.knud4.an.account.entity.Profile;
import lombok.Getter;

import javax.persistence.*;

@Getter
@MappedSuperclass
public class Board extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Profile writer;

    private Range range;
}
