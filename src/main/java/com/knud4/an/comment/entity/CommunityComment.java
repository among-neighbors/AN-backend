package com.knud4.an.comment.entity;

import com.knud4.an.account.entity.Profile;
import com.knud4.an.community.entity.Community;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityComment extends Comment {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @Builder
    public CommunityComment(Community community, String text, Profile writer) {
        this.community = community;
        this.setText(text);
        this.setWriter(writer);
    }

}