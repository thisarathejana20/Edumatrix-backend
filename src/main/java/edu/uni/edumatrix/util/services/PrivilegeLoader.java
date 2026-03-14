package edu.uni.edumatrix.util.services;

import edu.uni.edumatrix.dto.user.PrivilegeGroupDTO;
import lombok.Getter;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PrivilegeLoader {
    @Getter
    private final Set<String> allPrivilegeNames = new HashSet<>();

    @Getter
    private final List<PrivilegeGroupDTO> groups = new ArrayList<>();

    public void loadPrivileges(List<String> privileges, List<PrivilegeGroupDTO> groupedPrivileges) {
        allPrivilegeNames.clear();
        allPrivilegeNames.addAll(privileges);

        groups.clear();
        groups.addAll(groupedPrivileges);
    }
}
