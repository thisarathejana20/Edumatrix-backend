package edu.uni.edumatrix.dto.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PrivilegeDTO {
    private String action;
    private String name;
    private String description;
}
