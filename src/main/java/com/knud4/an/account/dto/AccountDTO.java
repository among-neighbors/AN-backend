package com.knud4.an.account.dto;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.entity.Role;
import com.knud4.an.house.entity.House;
import com.knud4.an.line.entity.Line;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    private Long id;
    private String email;
    private String username;
    private String lineName;
    private String houseName;
    private Role role;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.email = account.getEmail();
        this.username = account.getUsername();
        this.lineName = account.getLineName();
        this.houseName = account.getHouseName();
        this.role = account.getRole();
    }
}
