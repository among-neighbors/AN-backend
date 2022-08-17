package com.knud4.an.notice.entity;

import com.knud4.an.account.entity.Profile;
import com.knud4.an.board.Board;
import com.knud4.an.board.Range;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends Board {
    private LocalDateTime expiredDate;

    @Builder
    public Notice(String title, String content, Profile writer, Range range, LocalDateTime expiredDate) {
        this.setContent(content);
        this.setTitle(title);
        this.setWriter(writer);
        this.setRange(range);

        this.expiredDate = expiredDate;
    }
}
