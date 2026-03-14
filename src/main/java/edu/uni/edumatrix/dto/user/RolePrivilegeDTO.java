package edu.uni.edumatrix.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RolePrivilegeDTO {
    private String role;
    private List<String> privileges;
}

