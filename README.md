::: {align="center"}
# 📊 Tracker --- Team Task Manager

### Secure Full-Stack Task Tracking, Team Collaboration & Productivity Dashboard



[Backend
Repository](https://github.com/ashrithBalaji456/Tracker_Backend) •
[Frontend
Repository](https://github.com/ashrithBalaji456/Tracker_Frontend)

------------------------------------------------------------------------

## 📌 About the Project

**Tracker** is a secure full-stack team task management application
built to organize work, track progress, and visualize productivity
through a centralized dashboard.

The project combines a **React 18 + Vite** frontend with a **Spring Boot
3.3.5** REST backend. Authentication and protected API access are built
around **Spring Security and JWT**, while **PostgreSQL** stores
persistent application data.

The platform is designed around a simple workflow:

> **Authenticate → Create work → Organize tasks → Update progress →
> Visualize results**

It demonstrates practical full-stack engineering concepts including REST
API design, token-based authentication, protected routes, relational
persistence, frontend state management, API integration, validation, and
chart-based dashboards.

------------------------------------------------------------------------

## ✨ Key Features

### 🔐 Authentication & Authorization

-   User registration and login workflow.
-   Spring Security-based backend protection.
-   JWT token generation and validation.
-   Stateless authentication for REST APIs.
-   Protected frontend routes.
-   Axios-based authenticated API communication.

### ✅ Task Tracking

-   Create and manage tasks.
-   Organize work using task status and priority.
-   Update task progress throughout its lifecycle.
-   Track completion state.
-   Present task information through a responsive frontend interface.

### 👥 Team-Oriented Workflow

-   Structure work around users and team activity.
-   Track task ownership and progress.
-   Provide a centralized view of work status.
-   Support collaborative task-management workflows.

### 📈 Dashboard & Analytics

-   Display task and productivity data visually.
-   Use Recharts for interactive charts.
-   Summarize task status distribution.
-   Help users understand workload and completion progress.

### 🌐 Full-Stack API Integration

-   React communicates with Spring Boot using Axios.
-   React Router handles client-side navigation.
-   Spring controllers expose REST endpoints.
-   Service logic coordinates application workflows.
-   Spring Data JPA persists data in PostgreSQL.

------------------------------------------------------------------------

## 🧰 Technology Stack

  Layer             Technologies
  ----------------- ----------------------------------------
  Frontend          React 18.3.1
  Build Tool        Vite 5.4.11
  Routing           React Router DOM 6.28
  HTTP Client       Axios 1.7
  Charts            Recharts 2.13
  Icons             Lucide React
  Backend           Java 17, Spring Boot 3.3.5
  Web API           Spring Web, REST APIs
  Security          Spring Security
  Authentication    JWT using JJWT 0.12.6
  Validation        Spring Boot Validation
  Persistence       Spring Data JPA / Hibernate
  Database          PostgreSQL
  Build             Maven
  Testing           Spring Boot Test, Spring Security Test
  Version Control   Git & GitHub

------------------------------------------------------------------------

## 🏗️ High-Level Architecture

``` mermaid
flowchart LR
    U[User] --> FE[React Frontend]
    FE --> RR[React Router]
    RR --> PAGES[Pages & Dashboard]

    PAGES --> AX[Axios Client]
    AX -->|Bearer JWT + JSON| API[Spring Boot REST API]

    API --> SEC[Spring Security Filter Chain]
    SEC --> JWT[JWT Validation]
    JWT --> CTRL[Controllers]

    CTRL --> SERV[Service Layer]
    SERV --> REPO[Spring Data JPA]
    REPO --> DB[(PostgreSQL)]

    DB --> REPO
    REPO --> SERV
    SERV --> CTRL
    CTRL -->|JSON Response| AX

    SERV --> DATA[Task / User / Team Data]
    DATA --> CHARTS[Recharts Analytics]
```

------------------------------------------------------------------------

## 🔄 Complete Application Workflow

``` mermaid
flowchart TD
    A[User Opens Tracker] --> B{Authenticated?}

    B -- No --> C[Register or Login]
    C --> D[Frontend Sends Credentials]
    D --> E[Spring Security Authentication]
    E --> F{Valid Credentials?}

    F -- No --> G[Return Authentication Error]
    F -- Yes --> H[Generate JWT]
    H --> I[Frontend Stores Authentication State]
    I --> J[Open Protected Dashboard]

    B -- Yes --> J

    J --> K[Load Tasks and Dashboard Data]
    K --> L[Axios Sends Request with Bearer Token]
    L --> M[JWT Validation Filter]
    M --> N{Token Valid?}

    N -- No --> O[401 Unauthorized]
    N -- Yes --> P[Controller]
    P --> Q[Service Layer]
    Q --> R[Repository Layer]
    R --> S[(PostgreSQL)]

    S --> T[Return Persistent Data]
    T --> U[JSON API Response]
    U --> V[React Updates UI]
    V --> W[Recharts Visualizes Progress]
```

------------------------------------------------------------------------

## 🔐 JWT Authentication Flow

``` mermaid
sequenceDiagram
    actor User
    participant UI as React Frontend
    participant Auth as Auth API
    participant Security as Spring Security
    participant JWT as JWT Service
    participant DB as PostgreSQL

    User->>UI: Enter credentials
    UI->>Auth: POST login request
    Auth->>Security: Authenticate credentials
    Security->>DB: Load user data
    DB-->>Security: User record
    Security-->>Auth: Authentication success
    Auth->>JWT: Generate signed token
    JWT-->>Auth: JWT
    Auth-->>UI: Authentication response + token

    User->>UI: Open protected feature
    UI->>Auth: API request + Bearer JWT
    Auth->>JWT: Validate token
    JWT-->>Auth: Valid identity
    Auth->>DB: Execute authorized operation
    DB-->>Auth: Result
    Auth-->>UI: JSON response
```

### Authentication Request Lifecycle

``` text
Login Form
    ↓
POST Authentication Request
    ↓
Spring Security
    ↓
Credential Validation
    ↓
JWT Generation
    ↓
Token Returned to Frontend
    ↓
Authenticated Axios Request
    ↓
Authorization: Bearer <token>
    ↓
JWT Validation
    ↓
Security Context
    ↓
Protected Controller
    ↓
Service → Repository → PostgreSQL
```

------------------------------------------------------------------------

## 🧱 Backend Architecture

``` text
Tracker_Backend/
│
├── controller/       # REST endpoints
├── service/          # Business logic
├── repository/       # JPA database access
├── entity/           # Persistent domain models
├── dto/              # Request and response objects
├── security/         # Spring Security and JWT logic
├── config/           # Application configuration
├── exception/        # Error handling
└── application       # Spring Boot entry point
```

The backend follows separation of concerns:

``` mermaid
flowchart LR
    REQUEST[HTTP Request] --> SECURITY[Security Layer]
    SECURITY --> CONTROLLER[Controller]
    CONTROLLER --> SERVICE[Service]
    SERVICE --> REPOSITORY[Repository]
    REPOSITORY --> DB[(PostgreSQL)]

    DB --> REPOSITORY
    REPOSITORY --> SERVICE
    SERVICE --> CONTROLLER
    CONTROLLER --> RESPONSE[JSON Response]
```

  Layer        Responsibility
  ------------ ----------------------------------------------
  Security     Authenticates users and validates JWTs
  Controller   Handles HTTP requests and response codes
  Service      Contains application and task workflow logic
  Repository   Performs persistence operations
  Entity       Maps domain objects to database tables
  DTO          Defines API request and response contracts

------------------------------------------------------------------------

## 🎨 Frontend Architecture

``` text
Tracker_Frontend/
│
├── src/
│   ├── components/   # Reusable UI components
│   ├── pages/        # Login, dashboard and task views
│   ├── services/     # Axios API communication
│   ├── routes/       # Public and protected navigation
│   ├── context/      # Shared authentication/app state
│   └── App           # Application composition
│
├── package.json
└── vite.config
```

Frontend request flow:

``` mermaid
flowchart LR
    PAGE[React Page] --> COMPONENT[UI Component]
    COMPONENT --> SERVICE[API Service]
    SERVICE --> AXIOS[Axios]
    AXIOS --> API[Spring Boot API]
    API --> AXIOS
    AXIOS --> STATE[React State]
    STATE --> COMPONENT
    COMPONENT --> CHART[Recharts / UI]
```

------------------------------------------------------------------------

## 🗃️ Conceptual Data Model

``` mermaid
erDiagram
    USER ||--o{ TASK : owns
    USER ||--o{ TEAM_MEMBER : participates
    TEAM ||--o{ TEAM_MEMBER : contains
    TEAM ||--o{ TASK : organizes

    USER {
        bigint id PK
        string username
        string email
        string password_hash
        string role
    }

    TEAM {
        bigint id PK
        string name
        string description
    }

    TEAM_MEMBER {
        bigint id PK
        bigint user_id FK
        bigint team_id FK
        string member_role
    }

    TASK {
        bigint id PK
        string title
        text description
        string status
        string priority
        datetime due_date
        bigint assigned_user_id FK
        bigint team_id FK
    }
```

> The ER diagram documents the logical domain model of a team task
> tracker. Keep field names and relationships synchronized with the
> implementation as the project evolves.

------------------------------------------------------------------------

## 📊 Task Lifecycle

``` mermaid
stateDiagram-v2
    [*] --> TODO
    TODO --> IN_PROGRESS: Start work
    IN_PROGRESS --> TODO: Move back
    IN_PROGRESS --> COMPLETED: Finish task
    TODO --> COMPLETED: Complete directly
    COMPLETED --> IN_PROGRESS: Reopen
    COMPLETED --> [*]
```

A task typically moves through:

``` text
TODO
  ↓
IN_PROGRESS
  ↓
COMPLETED
```

Priority can be used independently to identify urgent work:

``` text
LOW → MEDIUM → HIGH
```

------------------------------------------------------------------------

## 📡 REST API Design

A typical API structure for the platform:

  Module           Base Route         Purpose
  ---------------- ------------------ -------------------------------
  Authentication   `/api/auth`        Registration and login
  Users            `/api/users`       User information
  Tasks            `/api/tasks`       Task CRUD and status updates
  Teams            `/api/teams`       Team and membership workflows
  Dashboard        `/api/dashboard`   Summary and analytics data

> Exact routes should remain aligned with the controller mappings in the
> backend implementation.

Example protected request:

``` http
GET /api/tasks
Authorization: Bearer <jwt-token>
```

Example task creation payload:

``` json
{
  "title": "Complete REST API integration",
  "description": "Connect the task dashboard with backend endpoints",
  "priority": "HIGH",
  "status": "TODO"
}
```

------------------------------------------------------------------------

## 📈 Dashboard Analytics Flow

``` mermaid
flowchart TD
    A[(PostgreSQL)] --> B[Repository Queries]
    B --> C[Service Aggregation]
    C --> D[Dashboard API]
    D --> E[Axios]
    E --> F[React State]
    F --> G[Recharts]

    G --> H[Status Distribution]
    G --> I[Completion Progress]
    G --> J[Priority Breakdown]
    G --> K[Team Productivity View]
```

Recharts enables the frontend to turn task data into interactive visual
summaries instead of showing only raw lists.

------------------------------------------------------------------------

## 🚀 Local Development Setup

### Prerequisites

Install:

-   Java 17+
-   Maven 3.9+
-   PostgreSQL
-   Node.js 18+
-   npm
-   Git

### 1. Clone the Backend

``` bash
git clone https://github.com/ashrithBalaji456/Tracker_Backend.git
cd Tracker_Backend
```

### 2. Create a PostgreSQL Database

``` sql
CREATE DATABASE tracker_db;
```

### 3. Configure Backend Environment

Use environment variables or your local Spring profile:

``` env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=tracker_db
DB_USER=postgres
DB_PASSWORD=your_password

JWT_SECRET=replace_with_a_long_secure_secret
JWT_EXPIRATION=86400000
```

Example Spring configuration:

``` properties
spring.datasource.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:tracker_db}
spring.datasource.username=${DB_USER:postgres}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> \[!WARNING\] Never commit PostgreSQL credentials, JWT secrets,
> production tokens, or `.env` files.

### 4. Run the Backend

``` bash
mvn spring-boot:run
```

Or with Maven Wrapper if present:

``` bash
./mvnw spring-boot:run
```

Windows:

``` bash
mvnw.cmd spring-boot:run
```

### 5. Clone the Frontend

Open a second terminal:

``` bash
git clone https://github.com/ashrithBalaji456/Tracker_Frontend.git
cd Tracker_Frontend
```

### 6. Install Frontend Dependencies

``` bash
npm install
```

### 7. Start Vite Development Server

``` bash
npm run dev
```

The terminal will display the local frontend address.

------------------------------------------------------------------------

## 🧪 Testing the Complete Flow

A practical end-to-end test order:

1.  Register a user.
2.  Log in with valid credentials.
3.  Verify that a JWT is returned.
4.  Access a protected endpoint using the Bearer token.
5.  Create a task.
6.  Fetch the task list.
7.  Change task status from `TODO` to `IN_PROGRESS`.
8.  Mark the task as `COMPLETED`.
9.  Verify PostgreSQL persistence.
10. Open the dashboard and verify that analytics reflect the updated
    task state.
11. Test an expired or invalid JWT and confirm that protected access is
    rejected.

------------------------------------------------------------------------

## 🔒 Security Design

The backend includes Spring Security and JJWT dependencies for stateless
token-based authentication.

``` mermaid
flowchart LR
    REQ[Request] --> FILTER[Security Filter Chain]
    FILTER --> TOKEN{JWT Present?}
    TOKEN -- No --> DENY[Reject Protected Request]
    TOKEN -- Yes --> VALIDATE[Validate Signature & Claims]
    VALIDATE -->|Invalid| DENY
    VALIDATE -->|Valid| CONTEXT[Set Authentication Context]
    CONTEXT --> ENDPOINT[Protected Endpoint]
```

Recommended production practices:

-   Store passwords using a strong adaptive password encoder.
-   Keep JWT secrets outside source control.
-   Use short-lived access tokens where appropriate.
-   Validate token signature and expiration.
-   Configure CORS only for trusted frontend origins.
-   Use HTTPS in production.
-   Return consistent authentication and authorization errors.
-   Avoid logging passwords and tokens.

------------------------------------------------------------------------

## 🧪 Testing Strategy

The backend includes Spring Boot Test and Spring Security Test
dependencies.

Recommended coverage:

-   Authentication service tests.
-   JWT generation and validation tests.
-   Unauthorized request tests.
-   Task service unit tests.
-   Repository integration tests.
-   Task ownership and authorization tests.
-   Controller integration tests.
-   Validation error tests.
-   PostgreSQL integration tests using Testcontainers.
-   Frontend API and protected-route tests.

------------------------------------------------------------------------

## 🧠 Engineering Concepts Demonstrated

-   Stateless JWT authentication
-   Spring Security filter-chain concepts
-   RESTful API design
-   Protected routes
-   Layered backend architecture
-   Dependency injection
-   Request validation
-   Spring Data JPA
-   ORM and relational persistence
-   PostgreSQL integration
-   React component architecture
-   Client-side routing
-   Axios interceptors and API communication
-   Dashboard data visualization
-   Frontend-backend integration
-   Maven and npm build workflows

------------------------------------------------------------------------

## 🗺️ Future Enhancements

-   [ ] Refresh-token authentication flow
-   [ ] Fine-grained role-based authorization
-   [ ] Email invitations for team members
-   [ ] Task comments and activity timeline
-   [ ] File attachments for tasks
-   [ ] Due-date notifications and reminders
-   [ ] WebSocket-based real-time updates
-   [ ] Advanced filtering, search, sorting and pagination
-   [ ] Kanban board with drag-and-drop
-   [ ] Audit logs for task changes
-   [ ] OpenAPI / Swagger documentation
-   [ ] Flyway database migrations
-   [ ] Docker Compose for frontend, backend and PostgreSQL
-   [ ] CI/CD with GitHub Actions
-   [ ] Testcontainers integration testing
-   [ ] Cloud deployment and managed PostgreSQL

------------------------------------------------------------------------

## 📂 Repositories

  --------------------------------------------------------------------------------------------------------------
  Component                           Repository
  ----------------------------------- --------------------------------------------------------------------------
  ⚙️ Backend                          [Tracker_Backend](https://github.com/ashrithBalaji456/Tracker_Backend)

  🎨 Frontend                         [Tracker_Frontend](https://github.com/ashrithBalaji456/Tracker_Frontend)
  --------------------------------------------------------------------------------------------------------------

------------------------------------------------------------------------

## 👨‍💻 Author

**Gudla Ashrith Balaji**

Java Backend Developer focused on Spring Boot, Spring Security, JWT
authentication, REST APIs, PostgreSQL, and full-stack application
development.

-   LinkedIn:
    https://www.linkedin.com/in/ashrith-balaji-gudla-5768302a8/
-   GitHub: https://github.com/ashrithBalaji456

------------------------------------------------------------------------

## ⭐ Support

If this project helps you or gives you ideas, consider starring the
repositories.


### Built with ☕ Java • 🌱 Spring Boot • ⚛️ React • 🐘 PostgreSQL

**Plan clearly. Track progress. Deliver together.**

