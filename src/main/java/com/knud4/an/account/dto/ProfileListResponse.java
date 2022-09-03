package com.knud4.an.account.dto;

import com.knud4.an.account.entity.Profile;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProfileListResponse {
    private final List<ProfileDTO> list;

    public ProfileListResponse(List<Profile> list) {
        this.list = list.stream().map(ProfileDTO::new)
                .collect(Collectors.toList());
    }
}
