package com.votingSystem.voting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "elections")
@Getter
@Setter
public class Election {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private LocalDateTime startTime, endTime;

    @OneToMany(mappedBy = "assignedElection", cascade = CascadeType.ALL)
    private List<Voter> voters;

    @OneToMany(mappedBy = "assignedElection", cascade = CascadeType.ALL)
    private List<Candidate> candidates;
}
