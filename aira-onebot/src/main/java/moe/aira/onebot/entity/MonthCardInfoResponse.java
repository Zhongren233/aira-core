package moe.aira.onebot.entity;

import lombok.Data;

@Data
public class MonthCardInfoResponse {
    private Result result;

    @Data
    public static class Result {
        private UserInfo user_info;
        private Integer state;
    }

    @Data
    public static class UserInfo {
        private Long user_id;
        private String head_icon;
        private Integer level;
        private String nick_name;
        private MonthCardInfo monthCardInfo;
        private MonthCardInfo monthCardPlusInfo;


    }

    @Data
    public static class MonthCardInfo {
        private Long expire_at;
        private Integer status;

        @Override
        public String toString() {
            return "MonthCardInfo{" +
                    "expire_at=" + expire_at +
                    ", status=" + (status == 1 ? "未领取" : "已领取") +
                    '}';
        }
    }


}
