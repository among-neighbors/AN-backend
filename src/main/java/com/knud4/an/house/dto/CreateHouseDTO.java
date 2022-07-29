package com.knud4.an.house.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateHouseDTO {
    private String lineName;
    private String houseName;
}
