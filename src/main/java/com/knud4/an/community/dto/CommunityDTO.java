package com.knud4.an.community.dto;

import com.knud4.an.board.Range;
import com.knud4.an.community.entity.Category;
import com.knud4.an.community.entity.Community;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDTO {

    private Long community_id;

    private String title;

    private String content;

    private Category category;

    private Range range;

    private String writerLineName;

    private String writerHouseName;

    private String writerName;

    public CommunityDTO(Community community) {
        this.community_id = community.getId();
        this.title = community.getTitle();
        this.content = community.getContent();
        this.category = community.getCategory();
        this.range = community.getRange();
        this.writerLineName = community.getWriterLineName();
        this.writerHouseName = community.getWriterHouseName();
        this.writerName = community.getWriter().getName();
    }

    public static List<CommunityDTO> entityListToDTOList(List<Community> communities) {
        List<CommunityDTO> communityDTOList = new ArrayList<>();
        for(Community community : communities) communityDTOList.add(new CommunityDTO(community));
        return communityDTOList;
    }
}
