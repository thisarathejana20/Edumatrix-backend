package edu.uni.edumatrix.util.services;

import edu.uni.edumatrix.dto.user.RolePrivilegeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class RolePrivilegeLoader {
    private final Map<String, List<String>> rolePrivileges = new HashMap<>();

    public Map<String, List<String>> getRolePrivileges() {
        return Collections.unmodifiableMap(rolePrivileges);
    }

    public void loadRolePrivileges(List<RolePrivilegeDTO> rolePrivilegeList) {
        log.info("Load Role Privileges");
        rolePrivileges.clear();
        for (RolePrivilegeDTO rp : rolePrivilegeList) {
            rolePrivileges.put(rp.getRole(), rp.getPrivileges());
        }
    }
}
