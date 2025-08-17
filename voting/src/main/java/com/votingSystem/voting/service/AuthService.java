package com.votingSystem.voting.service;

import com.votingSystem.voting.dto.auth.AuthenticationRequest;
import com.votingSystem.voting.dto.auth.AuthenticationResponse;
import com.votingSystem.voting.dto.auth.RegisterRequest;
import com.votingSystem.voting.entity.Role;
import com.votingSystem.voting.entity.Voter;
import com.votingSystem.voting.repository.VoterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final VoterRepository voterRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {

        // Set User And Save Him
        Voter voter = new Voter();
        voter.setName(request.getName());
        voter.setPassword(passwordEncoder.encode(request.getPassword()));
        voter.setEmail(request.getEmail());
        voter.setRole(Role.VOTER);
        voter.setVotes(new ArrayList<>());
        voterRepository.save(voter);

        // Create JWT Token
        String jwtToken = jwtService.generateToken(voter);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwtToken);

        // Send JWT Token
        return authenticationResponse;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        // add User Into Authentication Manager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Get User From DataBase
        Voter voter = voterRepository.findByEmail(request.getEmail()).orElseThrow();

        //  Set A JWT Token
        String jwtToken = jwtService.generateToken(voter);

        // Send JWT Token
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwtToken);
        return authenticationResponse;
    }
}
