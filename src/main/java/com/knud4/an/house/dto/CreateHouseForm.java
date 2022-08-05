package com.knud4.an.house.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateHouseForm {
    @NotNull
    private String lineName;
    @NotNull
    private String houseName;
}
