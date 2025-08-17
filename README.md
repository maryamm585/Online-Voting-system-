# Online-Voting-system-

### ERD
https://drive.google.com/file/d/1hga1l9Y8VmQM76VRba4k75CYGj2jovVg/view?usp=sharing

## Project Overview
The Online Voting System is a Spring Boot application that allows voters to register, log in, and cast their votes for candidates in specific elections. Administrators can manage candidates, assign voters to elections, and view election results.

The system ensures secure and fair voting using:
- JWT Authentication
- Role-Based Access Control
- Time Restrictions for elections

## Features

### Admin
- Register new candidates
- Assign voters to elections
- View election results in real-time

### Voter
- Register and log in
- View candidate list
- Cast a vote (one-time only)
- Vote only during the election period

### Security
- JWT-based authentication
- Role-based access: ROLE_ADMIN and ROLE_VOTER
- Prevention of double voting
- Time-restricted voting

## Tech Stack
- **Backend**: Spring Boot, Spring Data JPA, Spring Security
- **Authentication**: JWT
- **Database**: H2 (for testing) / MySQL (for production)
- **Validation**: Hibernate Validator
- **Utilities**: Lombok

## Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-repo/online-voting-system.git
   cd online-voting-system
#
   <h1>Run the Application</h1>
   ```bash
   mvn spring-boot:run

#
<h1>API Documentation</h1>

<h3>POST /api/v1/auth/register</h3>

{
  "firstname": "Ahmed",
  "lastname": "Mohamed",
  "email": "ahmed@example.com",
  "password": "123456"
}

response
{
  "token": "jwt_token_here"
}


<h2>POST /api/v1/auth/login</h2>
Request Body:

{
  "email": "ahmed@example.com",
  "password": "123456"
}

response
{
  "token": "jwt_token_here"
}

<h1>Voter Endpoints (/api/v1/voter)</h1>
<h3>GET /api/v1/voter/{voterId}/candidates</h3>
Response 

[
  {
    "id": 1,
    "name": "Candidate A",
    "party": "Party X"
  },
  {
    "id": 2,
    "name": "Candidate B",
    "party": "Party Y"
  }
]

<h1> Cast a Vote</h1>
<h3>POST /api/v1/voter/elections/{electionId}/vote</h3>
Request Body:
{
  "voterId": 5,
  "candidateId": 2
}
Response 
{
  "token": "jwt_token_here"
}

<h1>Get Election Results</h1>
<h3>GET /api/v1/voter/elections/{electionId}/results</h3>
Response:
{
  "Candidate A": 120,
  "Candidate B": 85,
  "Candidate C": 40
}

<h1>Admin Endpoints (/api/v1/admin)</h1>
1. Register a New Candidate
<h3>POST /api/v1/admin/candidates</h3>
Request Body:
{
  "name": "Candidate A",
  "party": "Party X"
}
Response
{
  "id": 1,
  "name": "Candidate A",
  "party": "Party X"
}
2. Assign Voter to Election
<h3>POST /api/v1/admin/voters/{voterId}/assign/{electionId}</h3>
Response:
{
  "id": 5,
  "firstname": "Ahmed",
  "lastname": "Mohamed",
  "assignedElection": 2
}
3. View Election Results
<h3>GET /api/v1/admin/results/{electionId}</h3>
Response
[
  {
    "candidate": "Candidate A",
    "votes": 120
  },
  {
    "candidate": "Candidate B",
    "votes": 85
  }
]
4. Add New Election
<h3>POST /api/v1/admin/addelections</h3>
Request Body:
{
  "title": "Presidential Election 2025",
  "description": "National election for presidency",
  "startDate": "2025-09-01T09:00:00",
  "endDate": "2025-09-05T18:00:00"
}

Response:
{
  "id": 2,
  "title": "Presidential Election 2025",
  "description": "National election for presidency",
  "startDate": "2025-09-01T09:00:00",
  "endDate": "2025-09-05T18:00:00"
}
