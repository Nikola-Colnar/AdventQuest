package oop.repository;

import oop.model.EventComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EventCommentsRepository extends JpaRepository<EventComments, Integer> {
}
