package com.votingSystem.voting.service;
import com.votingSystem.voting.dto.CandidateRegistrationDTO;
import com.votingSystem.voting.dto.VoterRegistrationDTO;
import com.votingSystem.voting.entity.Candidate;
import com.votingSystem.voting.entity.Election;
import com.votingSystem.voting.entity.Voter;
import com.votingSystem.voting.repository.CandidateRepository;
import com.votingSystem.voting.repository.ElectionRepository;
import com.votingSystem.voting.repository.VoterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
    private final CandidateRepository candidateRepository;
    private final VoterRepository voterRepository;
    private final ElectionRepository electionRepository;

    //register new candidate
    public CandidateRegistrationDTO registercandidate(CandidateRegistrationDTO dto) {
        Election election = electionRepository.findById(dto.getAssignedElectionId())
                .orElseThrow(() -> new RuntimeException("Election not found"));

        Candidate candidate = new Candidate();
        candidate.setName(dto.getName());
        candidate.setParty(dto.getParty());
        candidate.setAssignedElection(election);

        Candidate saved = candidateRepository.save(candidate);

        dto.setId(saved.getId());
        return dto;
    }

    public Election addElection(Election el) {
        Election election = new Election();
        election.setName(el.getName());
        election.setStartTime(el.getStartTime());
        election.setEndTime(el.getEndTime());

        Election saved = electionRepository.save(election);

        el.setId(saved.getId());
        return el;
    }

    // assign voter to an election
    public VoterRegistrationDTO assignvoter_to_election(Long voterId, Long electionId) {
        System.out.println("Assigning voter " + voterId + " to election " + electionId);

        Voter voter = voterRepository.findById(voterId)
                .orElseThrow(() -> new RuntimeException("Voter not found: " + voterId));

        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new RuntimeException("Election not found: " + electionId));

        System.out.println("Voter before: " + voter);
        System.out.println("Election: " + election);

        voter.setAssignedElection(election);
        Voter updated = voterRepository.save(voter);

        System.out.println("Voter after save: " + updated);

        VoterRegistrationDTO dto = new VoterRegistrationDTO();
        dto.setId(updated.getId());
        dto.setName(updated.getName());
        dto.setEmail(updated.getEmail());
        dto.setRole(updated.getRole().name());
        dto.setAssignedElectionId(election.getId());
        return dto;
    }


    public List<Map<String, Object>> viewresults(Long electionId) {
        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new RuntimeException("Election not found"));

        return election.getCandidates()
                .stream()
                .map(c -> {
                    Map<String, Object> result = new LinkedHashMap<>();
                    result.put("candidateId", c.getId());
                    result.put("name", c.getName());
                    result.put("party", c.getParty());
                    result.put("voteCount", c.getVotes() != null ? c.getVotes().size() : 0);
                    return result;
                })
                .sorted((a, b) -> ((Integer) b.get("voteCount")).compareTo((Integer) a.get("voteCount")))
                .collect(Collectors.toList());
    }
}

