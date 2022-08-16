package com.knud4.an.community.entity;

import com.knud4.an.board.Board;
import com.knud4.an.comment.entity.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Community extends Board {

    private Category category;

//    Comment와의 연관관계 추가 필요
//    @OneToMany
//    private List<Comment> comments;

    private Long commentCnt;

    private Long like;
}
