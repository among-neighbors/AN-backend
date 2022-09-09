package com.knud4.an.notice.dto;

import com.knud4.an.board.Scope;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNoticeForm {

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private LocalDateTime expiredDate;

    @NotNull
    private Scope scope;

    private String releaseLine;
}
