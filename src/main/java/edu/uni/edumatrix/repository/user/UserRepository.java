package edu.uni.edumatrix.repository.user;

import edu.uni.edumatrix.model.user.User;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE LOWER(u.query) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Optional<List<User>> searchUsers(@Param("keyword") String keyword, Limit of);
    Page<User> findAllByRole_NameNot(String excludeRole, Pageable pageable);
    Page<User> findByDesignationAndRole_NameNot(String designation, String excludedRole, Pageable pageable);
    @Query("""
       SELECT u FROM User u
       WHERE (:role IS NULL OR u.role.name = :role)
       AND (u.role IS NULL OR u.role.name <> :excludedRole)
       """)
    Page<User> findByRoleNameAndExclude(@Param("role") String role,
                                        @Param("excludedRole") String excludedRole,
                                        Pageable pageable);
    @Query("""
       SELECT u
       FROM User u
       WHERE (LOWER(u.fullName) LIKE CONCAT('%', :keyword, '%')
              OR LOWER(u.email) LIKE CONCAT('%', :keyword, '%'))
       AND (u.role IS NULL OR u.role.name <> :excludedRole)
       """)
    Page<User> searchUsers(@Param("keyword") String keyword,
                           @Param("excludedRole") String excludedRole,
                           Pageable pageable);
    Page<User> findByRole_NameAndDesignationAndRole_NameNot(
            String role, String designation, String excludedRole, Pageable pageable);
    @Query("""
        SELECT u
        FROM User u
        WHERE u.status = 'ACTIVE'
        AND (u.role IS NULL OR u.role.name <> 'ROOT_ADMIN')
        AND (
            u.designation IS NULL
            OR LOWER(u.designation) NOT LIKE 'ceo%'
            AND LOWER(u.designation) NOT LIKE 'director%'
        )
    """)
    List<User> findAllEligibleEmployees();
    List<User> findByRole_Name(String roleName);
}
