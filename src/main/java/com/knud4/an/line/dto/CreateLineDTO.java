package com.knud4.an.line.dto;

import com.sun.istack.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLineDTO {
    @NotNull
    private String name;
}
