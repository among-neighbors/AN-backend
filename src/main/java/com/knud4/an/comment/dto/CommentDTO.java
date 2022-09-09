package com.knud4.an.comment.dto;


import com.knud4.an.account.entity.Account;
import com.knud4.an.comment.entity.Comment;
import com.knud4.an.comment.entity.CommunityComment;
import com.knud4.an.comment.entity.ReportComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long id;

    private String text;

    private LocalDateTime createdDate;

    private Writer writer;

    private Boolean isMine;

    public CommentDTO(Comment comment, Long id) {
        this.id = id;
        this.text = comment.getText();
        this.createdDate = comment.getCreatedDate();
        Account account = comment.getWriter().getAccount();
        this.writer = new Writer(comment.getWriter().getId(),
                comment.getWriter().getName(),
                account.getLine().getName(),
                account.getHouse().getName());
    }

    public static List<CommentDTO> makeCommunityCommentList(List<CommunityComment> comments,
                                                            Long accountId) {
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (CommunityComment comment : comments) {
            CommentDTO commentDTO = new CommentDTO(comment, comment.getId());
            commentDTO.setIsMine(comment.getWriter().getAccount().getId().equals(accountId));
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }

    public static List<CommentDTO> makeReportCommentList(List<ReportComment> comments,
                                                         Long accountId) {
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for(ReportComment comment : comments) {
            CommentDTO commentDTO  = new CommentDTO(comment, comment.getId());
            commentDTO.setIsMine(comment.getWriter().getAccount().getId().equals(accountId));
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }

    @Data
    @AllArgsConstructor
    private static class Writer {
        Long id;
        String name, LineName, houseName;
    }
}
