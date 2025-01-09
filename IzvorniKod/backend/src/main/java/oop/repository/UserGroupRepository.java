package oop.repository;

import oop.model.UserGroup;
import oop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    List<UserGroup> findByGroupId(int groupId); // Dohvati sva imena po grupi
    List<UserGroup> findByUsername(String username); // Dohvati sve grupe po imenu

    // Provjera postoji li zapis s istim korisničkim imenom i grupom
    boolean existsByUsernameAndGroupId(String username, int groupId);

    UserGroup findByUsernameAndGroupId(String username, int groupId);
}
