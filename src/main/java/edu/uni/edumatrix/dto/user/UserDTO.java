package edu.uni.edumatrix.dto.user;

import edu.uni.edumatrix.model.user.User;
import lombok.Getter;
import lombok.Setter;

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


    public static UserDTO init(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setStatus(user.getStatus());
        userDTO.setEmpId(user.getEmpId());
        userDTO.setDesignation(user.getDesignation());
        userDTO.setMobile(user.getMobile());
        userDTO.setAddress(user.getAddress());

        return userDTO;
    }

}
