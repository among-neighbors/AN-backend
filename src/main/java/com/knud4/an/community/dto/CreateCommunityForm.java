package com.knud4.an.community.dto;

import com.knud4.an.community.entity.Category;
import com.knud4.an.board.Range;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommunityForm {

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private Category category;

    @NotNull
    private Range range;

}
