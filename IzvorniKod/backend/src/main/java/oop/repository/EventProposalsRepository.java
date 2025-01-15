package oop.repository;

import oop.model.EventProposals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventProposalsRepository extends JpaRepository<EventProposals, Integer> {
}
