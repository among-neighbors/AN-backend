package com.knud4.an.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class CommunityListDTO {

    @NotNull private Boolean isFirstPage;
    @NotNull private Boolean isLastPage;
    @NotNull private List<CommunityDTO> list;
}
