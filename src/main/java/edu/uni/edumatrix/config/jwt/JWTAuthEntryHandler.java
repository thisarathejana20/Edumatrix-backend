package edu.uni.edumatrix.config.jwt;

import edu.uni.edumatrix.util.exceptions.ExType;
import edu.uni.edumatrix.util.exceptions.http.UnauthorizedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import java.io.IOException;

public class JWTAuthEntryHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        final boolean isExpired = httpServletRequest.getAttribute("tok-expired") != null;
        final boolean isInactive = httpServletRequest.getAttribute("user-inactive") != null;
        if (isExpired) {
            httpServletResponse.getOutputStream().println(
                    new UnauthorizedException(ExType.TOKEN_EXPIRED, "Token is expired").getJsonAsString(null));
        } else if (isInactive) {
            httpServletResponse.getOutputStream().println(
                    new UnauthorizedException(ExType.USER_INACTIVE, "User is inactive").getJsonAsString(null));
        } else {
            httpServletResponse.getOutputStream().println(
                    new UnauthorizedException(ExType.TOKEN_INVALID, "Token is invalid").getJsonAsString(null));
        }
    }
}
