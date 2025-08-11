package com.votingSystem.voting.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "voters")
@Getter
@Setter
public class Voter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name, password, role;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "voter", cascade = CascadeType.ALL)
    private List<Vote> votes;

    @ManyToOne
    @JoinColumn(name = "election_id")
    private Election assignedElection;
}
