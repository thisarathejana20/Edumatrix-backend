package edu.uni.edumatrix.config;

import edu.uni.edumatrix.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;

public class Session {
    private Session() {}
    public static User getUser() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            return user;
        }
        return null;
    }

    public static void setUser(User user) {
        ArrayList<GrantedAuthority> authorityList = new ArrayList<>();
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, authorityList);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
