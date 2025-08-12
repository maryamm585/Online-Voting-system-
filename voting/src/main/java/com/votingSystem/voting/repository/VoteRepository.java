package com.votingSystem.voting.repository;

import com.votingSystem.voting.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {
    @Query("SELECT v FROM Vote v WHERE v.voter.id = :voterId")
    List<Vote> findVotesByVoterId(@Param("voterId") Long voterId);

    @Modifying
    @Query("DELETE FROM Vote v WHERE v.voter.id = :voterId")
    void deleteVotesByVoterId(@Param("voterId") Long voterId);

    boolean existsByVoterIdAndCandidateAssignedElectionId(Long voterId, Long electionId);

}
