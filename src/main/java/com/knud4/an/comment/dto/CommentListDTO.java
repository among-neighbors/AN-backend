package com.knud4.an.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentListDTO {

    @NotNull private Boolean isFirstPage;
    @NotNull private Boolean isLastPage;
    @NotNull private List<CommentDTO> list;
}
