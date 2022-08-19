package com.knud4.an.notice.dto;

import com.knud4.an.board.Range;
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

    private Long noticeId;

    private String title;

    private String content;

    private LocalDateTime expiredDate;

    private Range range;

    private LocalDateTime createdDate;

    private String writer;

    public NoticeDTO(Notice notice) {
        this.noticeId = notice.getId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.expiredDate = notice.getExpiredDate();
        this.range = notice.getRange();
        this.createdDate = notice.getCreatedDate();
        this.writer = notice.getWriter().getName();
    }

    public static List<NoticeDTO> entityListToDTOList(List<Notice> notices) {
        List<NoticeDTO> noticeDTOList = new ArrayList<>();
        for(Notice notice : notices) noticeDTOList.add(new NoticeDTO(notice));
        return noticeDTOList;
    }
}
