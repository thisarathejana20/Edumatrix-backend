package edu.uni.edumatrix.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountVerifyDTO {
    @NotBlank(message = "Token is required")
    private String token;
    @NotBlank(message = "OTP is required")
    private String otp;
}
