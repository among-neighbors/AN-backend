package com.knud4.an.community.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.knud4.an.board.Range;
import com.knud4.an.community.entity.Category;
import com.knud4.an.community.entity.Community;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDTO {

    private Long id;

    private String title;

    private String content;

    private Category category;

    private Range range;

    private Writer writer;

    private LocalDateTime createdDate;

    private Long like;


    private Boolean isMine;

    public CommunityDTO(Community community) {
        this.id = community.getId();
        this.title = community.getTitle();
        this.content = community.getContent();
        this.category = community.getCategory();
        this.range = community.getRange();
        this.writer = new Writer(community.getWriter().getId(),
                community.getWriter().getName(),
                community.getWriterLineName(),
                community.getWriterHouseName());
        this.createdDate = community.getCreatedDate();
        this.like = community.getLikes();
    }

    public static List<CommunityDTO> entityListToDTOList(List<Community> communities) {
        List<CommunityDTO> communityDTOList = new ArrayList<>();
        for(Community community : communities) communityDTOList.add(new CommunityDTO(community));
        return communityDTOList;
    }

    @Data
    @AllArgsConstructor
    private static class Writer {
        Long id;
        String name, lineName, houseName;
    }
}
