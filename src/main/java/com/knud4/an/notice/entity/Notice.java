package com.knud4.an.notice.entity;

import com.knud4.an.account.entity.Profile;
import com.knud4.an.board.Board;
import com.knud4.an.board.Scope;
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

    private String releaseLine;

    @Builder
    public Notice(String title, String content, Profile writer, Scope scope, LocalDateTime expiredDate, String releaseLine) {
        this.setContent(content);
        this.setTitle(title);
        this.setWriter(writer);
        this.setScope(scope);

        this.expiredDate = expiredDate;
        this.releaseLine = releaseLine;
    }

    public void changeExpiredDate(LocalDateTime expiredDate) {
        this.expiredDate = expiredDate;
    }

    public void changeReleaseLine(String releaseLine) {
        this.releaseLine = releaseLine;
    }
}
