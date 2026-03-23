# url-shortener
This project is a lightweight URL shortening system that converts long URLs into short, shareable links. It allows users to track click counts, view recent URLs, and access basic analytics. When a short URL is opened, the system redirects users to the original URL while recording usage data.
# Easy URL Shortener

A full-stack URL shortening application built with **React + TypeScript** (frontend) and **Spring Boot + Java 17** (backend).

## Features

- Shorten any URL and get a unique short link
- View recently shortened URLs with pagination
- Click a short URL to be redirected to the original (click count is tracked)
- Per-URL analytics with click history
- Statistics chart showing URL clicks and creations over time
- Responsive UI that works on desktop, tablet, and mobile
- Seed data loaded on startup for demonstration

## Project Structure

```
ReactProject/
├── backend/UrlShortenerApp/     # Spring Boot backend (Java 17, Maven)
│   └── src/main/java/UrlShortenerApp/
│       ├── controller/          # REST controllers
│       ├── dto/                 # Request/response DTOs
│       ├── entity/              # JPA entities (Url, ClickEvent)
│       ├── repository/          # Spring Data JPA repositories
│       ├── service/             # Business logic
│       └── config/              # CORS config, data seeder
├── url-shortener/               # React frontend (TypeScript)
│   └── src/
│       ├── api.ts               # API service layer
│       ├── App.tsx              # Root component with state management
│       └── components/          # UrlForm, UrlTable, Stats, styles
└── README.md
```

## Prerequisites

- **Java 17** or higher
- **Maven 3.8+** (or use the included Maven wrapper)
- **Node.js 18+** and **npm 9+**

## Setup & Run

### 1. Backend (Spring Boot)

```bash
cd ReactProject/backend/UrlShortenerApp

# Build and run
./mvnw spring-boot:run
# On Windows: mvnw.cmd spring-boot:run
```

The backend starts on **http://localhost:8080**. Seed data (14 URLs with click events) is automatically loaded on startup.

### 2. Frontend (React)

```bash
cd ReactProject/url-shortener

# Install dependencies
npm install

# Start dev server
npm start
```

The frontend starts on **http://localhost:3000** and proxies API requests to the backend.

## API Endpoints

| Method | Endpoint                    | Description                              |
|--------|-----------------------------|------------------------------------------|
| POST   | `/api/urls`                 | Create a new short URL                   |
| GET    | `/api/urls?page=1&per_page=10` | List recent URLs (paginated)          |
| GET    | `/api/urls/{id}/analytics`  | Get analytics for a specific URL         |
| GET    | `/api/stats`                | Get aggregated click/creation statistics |
| GET    | `/{shortCode}`              | Redirect to original URL (tracks click)  |

### Example: Create a Short URL

```bash
curl -X POST http://localhost:8080/api/urls \
  -H "Content-Type: application/json" \
  -d '{"originalUrl": "https://example.com"}'
```

Response:
```json
{
  "id": 1,
  "originalUrl": "https://example.com",
  "shortCode": "aBcDeF",
  "shortUrl": "http://localhost:8080/aBcDeF",
  "createdAt": "2026-03-19T10:30:00",
  "clickCount": 0,
  "lastAccessedAt": null
}
```

## Data Model

### Url
| Field          | Type           | Description              |
|----------------|----------------|--------------------------|
| id             | Long           | Primary key              |
| originalUrl    | String (2048)  | The original long URL    |
| shortCode      | String (unique)| Generated 6-char code    |
| createdAt      | LocalDateTime  | When the URL was created |
| clickCount     | int            | Total redirect count     |
| lastAccessedAt | LocalDateTime  | Last redirect timestamp  |

### ClickEvent
| Field     | Type          | Description                     |
|-----------|---------------|---------------------------------|
| id        | Long          | Primary key                     |
| urlId     | Long          | Foreign key to Url              |
| clickedAt | LocalDateTime | When the redirect happened      |

## Running Tests

### Backend Tests

```bash
cd ReactProject/backend/UrlShortenerApp
./mvnw test
```

Tests cover:
- URL creation (valid, invalid, auto-prepend http)
- URL listing with pagination and ordering
- Short URL redirect and click count increment
- Analytics endpoint with click history
- Statistics aggregation
- 404 handling for unknown codes/IDs

### Frontend Tests

```bash
cd ReactProject/url-shortener
npm test
```

## Technology Stack

| Layer    | Technology                              |
|----------|-----------------------------------------|
| Frontend | React 19, TypeScript, Recharts          |
| Backend  | Spring Boot 3.2, Java 17, Spring Data JPA |
| Database | H2 (in-memory)                          |
| Build    | Maven (backend), npm/CRA (frontend)     |

## H2 Database Console

While the backend is running, access the H2 console at:
**http://localhost:8080/h2-console**

- JDBC URL: `jdbc:h2:mem:urlsdb`
- Username: `sa`
- Password: *(empty)*
