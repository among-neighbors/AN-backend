package com.knud4.an.notice.dto;

import com.knud4.an.board.Scope;
import com.knud4.an.notice.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDTO {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime expiredDate;

    private Scope scope;

    private LocalDateTime createdDate;

    private Writer writer;

    private String releaseLine;


    private Boolean isMine;

    public NoticeDTO(Notice notice) {
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.expiredDate = notice.getExpiredDate();
        this.scope = notice.getScope();
        this.createdDate = notice.getCreatedDate();
        this.releaseLine = notice.getReleaseLine();
        this.writer = new Writer(notice.getWriter().getId(),
                notice.getWriter().getName());
    }

    public static List<NoticeDTO> entityListToDTOList(List<Notice> notices) {
        List<NoticeDTO> noticeDTOList = new ArrayList<>();
        for(Notice notice : notices) noticeDTOList.add(new NoticeDTO(notice));
        return noticeDTOList;
    }

    @Data
    @AllArgsConstructor
    private static class Writer {
        Long id;
        String name;
    }
}
