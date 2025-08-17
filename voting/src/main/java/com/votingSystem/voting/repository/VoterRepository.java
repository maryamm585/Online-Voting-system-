package com.votingSystem.voting.repository;

import com.votingSystem.voting.entity.Voter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoterRepository extends JpaRepository <Voter,Long> {
    Optional<Voter> findByEmail(String email);

}
