package com.knud4.an.community.entity;

import com.knud4.an.account.entity.Profile;
import com.knud4.an.board.Board;
import com.knud4.an.board.Range;
import com.knud4.an.comment.entity.Comment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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

    @Builder
    public Community(String title, String content, Profile writer,
                     Range range, Category category, List<Comment> comments,
                     Long commentCnt, Long likes) {
        this.setContent(content);
        this.setTitle(title);
        this.setWriter(writer);
        this.setRange(range);

        this.category = category;
        this.comments = comments;
        this.commentCnt = commentCnt;
        this.likes = likes;
    }
}
