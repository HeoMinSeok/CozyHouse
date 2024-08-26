package com.mycozyhouse.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    private String email;
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
    private String phone;
    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;
    private ProviderType provider;
    private UserStatus status;
    @NotNull
    private String verificationCode;
}
