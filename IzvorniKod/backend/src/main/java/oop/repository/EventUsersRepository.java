package oop.repository;

import oop.model.EventUserId;
import oop.model.EventUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventUsersRepository extends JpaRepository<EventUsers, EventUserId> {
}
