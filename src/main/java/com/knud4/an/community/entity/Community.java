package com.knud4.an.community.entity;

import com.knud4.an.board.Board;
import com.knud4.an.comment.entity.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Community extends Board {

    private Category category;

    @OneToMany(mappedBy = "community")
    private List<Comment> comments;

    private Long commentCnt;

    private Long likes;
}
