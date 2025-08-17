package com.votingSystem.voting.service;

import com.votingSystem.voting.dto.CandidateRegistrationDTO;
import com.votingSystem.voting.dto.VoteDTO;
import com.votingSystem.voting.entity.Candidate;
import com.votingSystem.voting.entity.Election;
import com.votingSystem.voting.entity.Vote;
import com.votingSystem.voting.entity.Voter;
import com.votingSystem.voting.exception.DoubleVotingException;
import com.votingSystem.voting.exception.VoteNotFoundException;
import com.votingSystem.voting.exception.VotingClosedException;
import com.votingSystem.voting.repository.CandidateRepository;
import com.votingSystem.voting.repository.ElectionRepository;
import com.votingSystem.voting.repository.VoteRepository;
import com.votingSystem.voting.repository.VoterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VoterService {

    private final VoteRepository voteRepository;
    private final VoterRepository voterRepository;
    private final CandidateRepository candidateRepository;
    private final ElectionRepository electionRepository;

    public List<CandidateRegistrationDTO> getcandidatesbyvoterid(Long voterId) {
        Voter voter = voterRepository.findById(voterId)
                .orElseThrow(() -> new VoteNotFoundException("Voter not found"));

        Election election = voter.getAssignedElection();
        if (election == null) {
            throw new VotingClosedException("You are not assigned to any election");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(election.getStartTime()) || now.isAfter(election.getEndTime())) {
            throw new VotingClosedException("Voting is currently closed.");
        }

        return election.getCandidates()
                .stream()
                .map(c -> {
                    CandidateRegistrationDTO dto = new CandidateRegistrationDTO();
                    dto.setId(c.getId());
                    dto.setName(c.getName());
                    dto.setParty(c.getParty());
                    dto.setAssignedElectionId(election.getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public void castvote(VoteDTO dto) {
        Voter voter = voterRepository.findById(dto.getVoterId())
                .orElseThrow(() -> new VoteNotFoundException("Voter not found"));

        Candidate candidate = candidateRepository.findById(dto.getCandidateId())
                .orElseThrow(() -> new VoteNotFoundException("Candidate not found"));

        Election election = voter.getAssignedElection();
        if (election == null) {
            throw new VotingClosedException("You are not assigned to any election");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(election.getStartTime()) || now.isAfter(election.getEndTime())) {
            throw new VotingClosedException("Voting is currently closed.");
        }

        boolean alreadyVoted = voteRepository.existsByVoterIdAndElectionId(
                voter.getId(), election.getId()
        );
        if (alreadyVoted) {
            throw new DoubleVotingException("You have already voted.");
        }

        Vote vote = new Vote();
        vote.setVoter(voter);
        vote.setCandidate(candidate);
        vote.setTimeStamp(now);

        vote.setElection(election);

        voteRepository.save(vote);
    }

    public Map<String, Long> getResults(Long electionId) {
        List<Object[]> results = voteRepository.countVotesByCandidateNameAndElectionId(electionId);

        Map<String, Long> sortedResults = new LinkedHashMap<>();
        for (Object[] result : results) {
            String candidateName = (String) result[0];
            Long voteCount = (Long) result[1];
            sortedResults.put(candidateName, voteCount);
        }
        return sortedResults;
    }
}
