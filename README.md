# Online-Voting-system-

### ERD
https://drive.google.com/file/d/1hga1l9Y8VmQM76VRba4k75CYGj2jovVg/view?usp=sharing

---

## 📖 Project Overview
The **Online Voting System** is a Spring Boot application that allows voters to register, log in, and cast their votes for candidates in specific elections. Administrators can manage candidates, assign voters to elections, and view election results.  

The system ensures secure and fair voting using:
- **JWT Authentication**
- **Role-Based Access Control**
- **Time Restrictions** for elections

---

## 🚀 Features

### 👨‍💼 Admin
- Register new candidates.
- Assign voters to elections.
- View election results in real-time.

### 🧑‍💻 Voter
- Register and log in.
- View candidate list.
- Cast a vote (one-time only).
- Vote only during the election period.

### 🔒 Security
- JWT-based authentication.
- Role-based access: `ROLE_ADMIN` and `ROLE_VOTER`.
- Prevention of double voting.
- Time-restricted voting.

---

## 🛠 Tech Stack
- **Backend:** Spring Boot, Spring Data JPA, Spring Security
- **Authentication:** JWT
- **Database:** H2 (for testing) / MySQL (for production)
- **Validation:** Hibernate Validator
- **Utilities:** Lombok

---

## 📂 Project Structure

```plaintext
src/
└── main/
    ├── java/
    │   └── com/example/voting/
    │       ├── config/
    │       │   ├── SecurityConfig.java
    │       │   ├── JwtAuthenticationEntryPoint.java
    │       │   ├── JwtRequestFilter.java
    │       │   └── WebConfig.java
    │       │
    │       ├── controller/
    │       │   ├── AdminController.java
    │       │   ├── VoterController.java
    │       │   └── AuthController.java
    │       │
    │       ├── dto/
    │       │   ├── CandidateRegistrationDTO.java
    │       │   ├── VoterRegistrationDTO.java
    │       │   ├── LoginRequestDTO.java
    │       │   ├── JwtResponseDTO.java
    │       │   └── VoteDTO.java
    │       │
    │       ├── entity/
    │       │   ├── Candidate.java
    │       │   ├── Election.java
    │       │   ├── Voter.java
    │       │   └── Vote.java
    │       │
    │       ├── exception/
    │       │   ├── GlobalExceptionHandler.java
    │       │   ├── ResourceNotFoundException.java
    │       │   ├── InvalidCredentialsException.java
    │       │   ├── DoubleVotingException.java
    │       │   └── VotingClosedException.java
    │       │
    │       ├── repository/
    │       │   ├── CandidateRepository.java
    │       │   ├── ElectionRepository.java
    │       │   ├── VoterRepository.java
    │       │   └── VoteRepository.java
    │       │
    │       ├── security/
    │       │   ├── JwtTokenUtil.java
    │       │   └── CustomUserDetailsService.java
    │       │
    │       ├── service/
    │       │   ├── AdminService.java
    │       │   └── VoterService.java
    │       │
    │       └── VotingApplication.java
    │
    └── resources/
        ├── application.properties
        ├── application-dev.properties
        └── schema.sql
