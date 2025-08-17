package com.votingSystem.voting.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VoteDTO {
      private Long id;

    private LocalDateTime timeStamp; // can be set by backend when saving

    @NotNull(message = "Voter ID is required")
    private Long voterId;

    @NotNull(message = "Candidate ID is required")
    private Long candidateId;
}
