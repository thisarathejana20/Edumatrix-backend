package edu.uni.edumatrix.dto.user;

import edu.uni.edumatrix.model.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class UserDTO {
    private String id;
    private String fullName;
    private String email;
    private String mobile;
    private String address;
    private String status;
    private String empId;
    private String designation;
    private List<String> privileges;
    private Map<String, List<String>> permissions = new HashMap<>();


    public static UserDTO init(User user, List<PrivilegeGroupDTO> groups) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setStatus(user.getStatus());
        userDTO.setEmpId(user.getEmpId());
        userDTO.setDesignation(user.getDesignation());
        userDTO.setMobile(user.getMobile());
        userDTO.setAddress(user.getAddress());
        Map<String, List<String>> permissions = new HashMap<>();
        permissions.put("moduleView", extractModules(user.getPrivileges(), groups));
        permissions.put("moduleActions", extractActions(user.getPrivileges(), groups));
        userDTO.setPermissions(permissions);

        return userDTO;
    }

    private static List<String> extractModules(List<String> userPrivileges, List<PrivilegeGroupDTO> groups) {
        return groups.stream()
                .filter(g -> g.getPrivileges().stream().anyMatch(p -> userPrivileges.contains(p.getAction())))
                .map(PrivilegeGroupDTO::getModule)
                .distinct()
                .toList();
    }

    private static List<String> extractActions(List<String> userPrivileges, List<PrivilegeGroupDTO> groups) {
        return groups.stream()
                .flatMap(g -> g.getPrivileges().stream())
                .filter(p -> userPrivileges.contains(p.getAction()))
                .map(PrivilegeDTO::getAction)
                .distinct()
                .toList();
    }

    public static List<UserDTO> init(List<User> users, List<PrivilegeGroupDTO> groups) {
        return users.stream().map(user -> init(user, groups)).toList();
    }

}
