package oop.repository;

import oop.model.RatedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatedEventRepository extends JpaRepository<RatedEvent, Integer> {
}
