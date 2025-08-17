package com.votingSystem.voting.controller;
import com.votingSystem.voting.dto.CandidateRegistrationDTO;
import com.votingSystem.voting.dto.VoterRegistrationDTO;
import com.votingSystem.voting.entity.Election;
import com.votingSystem.voting.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/candidates")
    public ResponseEntity<CandidateRegistrationDTO> registerCandidate(@Valid @RequestBody CandidateRegistrationDTO dto) {
        CandidateRegistrationDTO saved = adminService.registercandidate(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/voters/{voterId}/assign/{electionId}")
    public ResponseEntity<VoterRegistrationDTO> assignVoter(
            @PathVariable Long voterId,
            @PathVariable Long electionId) {
        VoterRegistrationDTO updated = adminService.assignvoter_to_election(voterId, electionId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/results/{electionId}")
    public ResponseEntity<List<Map<String, Object>>> viewResults(@PathVariable Long electionId) {
        List<Map<String, Object>> results = adminService.viewresults(electionId);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/addelections")
    public ResponseEntity<Election> addElection(@Valid @RequestBody Election el) {
        Election saved = adminService.addElection(el);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

}
