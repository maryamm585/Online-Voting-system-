package com.votingSystem.voting.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="candidates")
@Getter
@Setter
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name, party;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    private List<Vote> votes;

    @ManyToOne
    @JoinColumn(name = "election_id")
    private Election assignedElection;
}
