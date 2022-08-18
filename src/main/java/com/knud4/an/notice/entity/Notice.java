package com.knud4.an.notice.entity;

import com.knud4.an.account.entity.Profile;
import com.knud4.an.board.Board;
import com.knud4.an.community.entity.Range;
import com.knud4.an.line.entity.Line;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends Board {
    private LocalDateTime expiredDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Line releaseLine;  // null이면 전체공개

    @Builder
    public Notice(String title, String content, Profile writer, Range range, LocalDateTime expiredDate, Line releaseLine) {
        this.setContent(content);
        this.setTitle(title);
        this.setWriter(writer);

        this.expiredDate = expiredDate;
        this.releaseLine = releaseLine;
    }
}
