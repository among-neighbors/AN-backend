package com.knud4.an.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommunityListDTO {

    private boolean isLastPage;
    private List<CommunityDTO> communityList;
}
