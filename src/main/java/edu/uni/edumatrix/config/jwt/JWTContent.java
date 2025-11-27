package edu.uni.edumatrix.config.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@Setter
@Getter
public class JWTContent {
    private String subject;
    private Map<String, String> payload;
    private float expiredIn;
}
