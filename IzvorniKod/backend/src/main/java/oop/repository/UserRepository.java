package oop.repository;

import oop.model.Group;
import oop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    User findByEmail(String email);

    @Query("SELECT s.groups FROM User s WHERE s.id = :id")
    Set<Group> getGroupsByUserId(@Param("id") int id);
}