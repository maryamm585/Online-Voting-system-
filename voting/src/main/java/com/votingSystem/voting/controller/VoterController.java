package com.votingSystem.voting.controller;

import com.votingSystem.voting.dto.CandidateRegistrationDTO;
import com.votingSystem.voting.dto.VoteDTO;
import com.votingSystem.voting.service.VoterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/voter")
@RequiredArgsConstructor
public class VoterController {
    private final VoterService voterService;

    @GetMapping("/{voterId}/candidates")
    public ResponseEntity<List<CandidateRegistrationDTO>> getCandidates(@PathVariable Long voterId) {
        List<CandidateRegistrationDTO> candidates = voterService.getcandidatesbyvoterid(voterId);
        return ResponseEntity.ok(candidates);
    }

    @PostMapping("/vote")
    public ResponseEntity<Map<String, String>> castVote(@Valid @RequestBody VoteDTO voteDTO) {
        voterService.castvote(voteDTO);
        return ResponseEntity.ok(Map.of("message", "Vote cast successfully"));
    }
}
