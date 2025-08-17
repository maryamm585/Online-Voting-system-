package com.votingSystem.voting.repository;

import com.votingSystem.voting.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("SELECT v FROM Vote v WHERE v.voter.id = :voterId")
    List<Vote> findVotesByVoterId(@Param("voterId") Long voterId);

    @Modifying
    @Query("DELETE FROM Vote v WHERE v.voter.id = :voterId")
    void deleteVotesByVoterId(@Param("voterId") Long voterId);

    boolean existsByVoterIdAndElectionId(Long voterId, Long electionId);

    @Query("SELECT v.candidate.name, COUNT(v.id) FROM Vote v WHERE v.election.id = ?1 GROUP BY v.candidate.name ORDER BY COUNT(v.id) DESC")
    List<Object[]> countVotesByCandidateNameAndElectionId(Long electionId);
}
