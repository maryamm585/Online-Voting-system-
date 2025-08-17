package com.votingSystem.voting.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateRegistrationDTO {
      private Long id;

    @NotBlank(message = "Candidate name is required")
    private String name;

    @NotBlank(message = "Party is required")
    private String party;

    @NotNull(message = "Assigned election ID is required")
    private Long assignedElectionId;
}
