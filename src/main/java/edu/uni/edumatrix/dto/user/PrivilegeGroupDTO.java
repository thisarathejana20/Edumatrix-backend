package edu.uni.edumatrix.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PrivilegeGroupDTO {
    private String group;
    private String module;
    private List<PrivilegeDTO> privileges;
}
