package edu.uni.edumatrix.util.services;

import edu.uni.edumatrix.util.annotations.RequirePrivileges;
import edu.uni.edumatrix.util.exceptions.http.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
public class PrivilegeAspect {
    private final AuthCheckService authCheckService;

    @Before("@annotation(requirePrivileges)")
    public void checkPrivileges(RequirePrivileges requirePrivileges) {
        List<String> required = Arrays.asList(requirePrivileges.value());

        if (!authCheckService.hasAllPrivileges(required)) {
            throw new AccessDeniedException("User lacks required privileges");
        }
    }
}
