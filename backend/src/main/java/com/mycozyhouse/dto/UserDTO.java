package com.mycozyhouse.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Statement;

@Data
@Builder
////JSON으로 변환할 때 null 값인 필드는 제외
//@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {


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
