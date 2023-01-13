package com.bitstudy.app.dto;

import com.bitstudy.app.domain.UserAccount;

import java.time.LocalDateTime;

public record UserAccountDto(
//        Long id,
        String userId,
        String userPassword,
        String email,
        String nickname,
        String memo,
        LocalDateTime registerDate,
        String createdBy,
        LocalDateTime modifiedDate,
        String modifiedBy
) {
    public static UserAccountDto of(
                                    String userId,
                                    String userPassword,
                                    String email,
                                    String nickname,
                                    String memo,
                                    LocalDateTime registerDate,
                                    String createdBy,
                                    LocalDateTime modifiedDate,
                                    String modifiedBy) {
        return new UserAccountDto(userId, userPassword, email, nickname, memo, registerDate, createdBy, modifiedDate, modifiedBy);
    }
    public static UserAccountDto of(
                                    String userId,
                                    String userPassword,
                                    String email,
                                    String nickname,
                                    String memo) {
        return new UserAccountDto(userId, userPassword, email, nickname, memo, null, null, null, null);
    }
    public static UserAccountDto from(UserAccount entity){
        return new UserAccountDto(
//                entity.getId(),
                entity.getUserId()
                , entity.getUserPassword()
                , entity.getEmail()
                , entity.getNickname()
                , entity.getMemo()
                , entity.getRegisterDate()
                , entity.getCreatedBy()
                , entity.getModifiedDate()
                , entity.getModifiedBy()
        );
    }

    public UserAccount toEntity() {
        return UserAccount.of(this.userId, this.userPassword, this.email, this.nickname, this.memo);
    }
}
