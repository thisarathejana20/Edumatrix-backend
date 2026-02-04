package edu.uni.edumatrix.repository.user;

import edu.uni.edumatrix.model.user.User;
import org.springframework.data.domain.Limit;
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
}
