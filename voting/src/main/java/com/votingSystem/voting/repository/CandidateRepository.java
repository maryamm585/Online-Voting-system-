package com.votingSystem.voting.repository;

import com.votingSystem.voting.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate,Long> {
    List<Candidate> findByNameContaining(String name);
}
