package com.knud4.an.comment.dto;


import com.knud4.an.account.entity.Account;
import com.knud4.an.comment.entity.Comment;
import com.knud4.an.comment.entity.CommunityComment;
import com.knud4.an.comment.entity.ReportComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private String text;

    private String writerName;

    private String writerLineName;

    private String writerHouseName;

    public CommentDTO(Comment comment) {
        this.text = comment.getText();
        this.writerName = comment.getWriter().getName();
        Account account = comment.getWriter().getAccount();
        this.writerLineName = account.getLine().getName();
        this.writerHouseName = account.getHouse().getName();
    }

    public static List<CommentDTO> makeCommunityCommentList(List<CommunityComment> comments) {
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for(CommunityComment comment : comments) commentDTOList.add(new CommentDTO(comment));
        return commentDTOList;
    }

    public static List<CommentDTO> makeReportCommentList(List<ReportComment> comments) {
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for(ReportComment comment : comments) commentDTOList.add(new CommentDTO(comment));
        return commentDTOList;
    }
}
