package edu.uni.edumatrix.util.services;

import edu.uni.edumatrix.config.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthCheckService {

    public boolean hasAllPrivileges(List<String> requiredPrivileges) {
        log.info("Check privileges");
        List<String> userPrivileges = Session.getUser().getPrivileges();
        return userPrivileges.stream().anyMatch(requiredPrivileges::contains);
    }
}
