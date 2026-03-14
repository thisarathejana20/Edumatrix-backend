package edu.uni.edumatrix.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetDTO {
    @NotBlank(message = "Token is required")
    private String token;
    @NotBlank(message = "New Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&^#\\-_=+]).{6,}$",
            message = "At least 6 characters with one digit, one lower case letter, one upper case letter, and one symbol"
    )
    private String newPassword;
}
