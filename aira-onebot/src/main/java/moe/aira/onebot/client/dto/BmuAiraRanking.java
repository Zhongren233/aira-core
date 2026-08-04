package moe.aira.onebot.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@lombok.Data
public class BmuAiraRanking {
    @lombok.Getter(onMethod_ = {@JsonProperty("point_ranking")})
    @lombok.Setter(onMethod_ = {@JsonProperty("point_ranking")})
    private Ranking pointRanking;
    @lombok.Getter(onMethod_ = {@JsonProperty("score_ranking")})
    @lombok.Setter(onMethod_ = {@JsonProperty("score_ranking")})
    private Ranking scoreRanking;
    @lombok.Getter(onMethod_ = {@JsonProperty("user_profile")})
    @lombok.Setter(onMethod_ = {@JsonProperty("user_profile")})
    private UserProfile userProfile;
    @lombok.Getter(onMethod_ = {@JsonProperty("warnings")})
    @lombok.Setter(onMethod_ = {@JsonProperty("warnings")})
    private Warnings warnings;
    @lombok.Getter(onMethod_ = {@JsonProperty("error")})
    @lombok.Setter(onMethod_ = {@JsonProperty("error")})
    private String error;

// Ranking.java


    @lombok.Data
    public static class Ranking {
        @lombok.Getter(onMethod_ = {@JsonProperty("UserID")})
        @lombok.Setter(onMethod_ = {@JsonProperty("UserID")})
        private Integer userid;
        @lombok.Getter(onMethod_ = {@JsonProperty("EventID")})
        @lombok.Setter(onMethod_ = {@JsonProperty("EventID")})
        private Integer eventid;
        @lombok.Getter(onMethod_ = {@JsonProperty("EventPoint")})
        @lombok.Setter(onMethod_ = {@JsonProperty("EventPoint")})
        private Long eventPoint;
        @lombok.Getter(onMethod_ = {@JsonProperty("EventRank")})
        @lombok.Setter(onMethod_ = {@JsonProperty("EventRank")})
        private Integer eventRank;
        @lombok.Getter(onMethod_ = {@JsonProperty("CreatedAt")})
        @lombok.Setter(onMethod_ = {@JsonProperty("CreatedAt")})
        private Date createdAt;
        @lombok.Getter(onMethod_ = {@JsonProperty("UpdatedAt")})
        @lombok.Setter(onMethod_ = {@JsonProperty("UpdatedAt")})
        private Date updatedAt;
    }

// UserProfile.java


    @lombok.Data
    public static class UserProfile {
        @lombok.Getter(onMethod_ = {@JsonProperty("UserID")})
        @lombok.Setter(onMethod_ = {@JsonProperty("UserID")})
        private Integer userid;
        @lombok.Getter(onMethod_ = {@JsonProperty("EventID")})
        @lombok.Setter(onMethod_ = {@JsonProperty("EventID")})
        private Integer eventid;
        @lombok.Getter(onMethod_ = {@JsonProperty("UserName")})
        @lombok.Setter(onMethod_ = {@JsonProperty("UserName")})
        private String userName;
        @lombok.Getter(onMethod_ = {@JsonProperty("UserFavoriteCardID")})
        @lombok.Setter(onMethod_ = {@JsonProperty("UserFavoriteCardID")})
        private Integer userFavoriteCardid;
        @lombok.Getter(onMethod_ = {@JsonProperty("UserFavoriteCardEvolved")})
        @lombok.Setter(onMethod_ = {@JsonProperty("UserFavoriteCardEvolved")})
        private Integer userFavoriteCardEvolved;
        @lombok.Getter(onMethod_ = {@JsonProperty("UserAward1ID")})
        @lombok.Setter(onMethod_ = {@JsonProperty("UserAward1ID")})
        private Integer userAward1id;
        @lombok.Getter(onMethod_ = {@JsonProperty("UserAward1Value")})
        @lombok.Setter(onMethod_ = {@JsonProperty("UserAward1Value")})
        private Integer userAward1Value;
        @lombok.Getter(onMethod_ = {@JsonProperty("UserAward2ID")})
        @lombok.Setter(onMethod_ = {@JsonProperty("UserAward2ID")})
        private Integer userAward2id;
        @lombok.Getter(onMethod_ = {@JsonProperty("UserAward2Value")})
        @lombok.Setter(onMethod_ = {@JsonProperty("UserAward2Value")})
        private Integer userAward2Value;
        @lombok.Getter(onMethod_ = {@JsonProperty("Location")})
        @lombok.Setter(onMethod_ = {@JsonProperty("Location")})
        private String location;
        @lombok.Getter(onMethod_ = {@JsonProperty("CreatedAt")})
        @lombok.Setter(onMethod_ = {@JsonProperty("CreatedAt")})
        private Date createdAt;
        @lombok.Getter(onMethod_ = {@JsonProperty("UpdatedAt")})
        @lombok.Setter(onMethod_ = {@JsonProperty("UpdatedAt")})
        private Date updatedAt;
    }

// Warnings.java


    @lombok.Data
    public static class Warnings {
        @lombok.Getter(onMethod_ = {@JsonProperty("score_ranking")})
        @lombok.Setter(onMethod_ = {@JsonProperty("score_ranking")})
        private String scoreRanking;

        @lombok.Getter(onMethod_ = {@JsonProperty("point_ranking")})
        @lombok.Setter(onMethod_ = {@JsonProperty("point_ranking")})
        private String pointRanking;
    }
}