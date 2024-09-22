package com.mycozyhouse.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
    private String phone;
    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;
    private ProviderType provider;
    private UserStatus status;
//    @NotNull
    private String verificationCode;
}
