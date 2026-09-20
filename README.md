# Activity Finder

A web application for finding company for everyday activities. A user posts something
they are doing — a bike ride on Saturday, a board-game evening, a hike — sets how many
people are needed, and others join. Each activity has its own group chat so participants
can agree on the details before meeting.

Unlike an events aggregator, the content comes from the users themselves, so the service
is useful from the very first accounts.

## Stack

Java 21 · Spring Boot 3 · Spring Data JPA · Spring Security (JWT) · PostgreSQL · Docker · plain HTML/CSS/JS frontend

## Features

- Registration and login with JWT; passwords stored as BCrypt hashes
- Activity feed with a category filter
- Creating an activity — the author automatically becomes its first participant
- Joining and leaving, with a capacity check
- Group chat, visible only to the participants of that activity
- User profiles

## Engineering notes

**Authorisation is checked per object, not only per role.** Being authenticated is not
enough to read an activity's chat — `MessageService` verifies that the caller is an actual
participant of that activity and returns 403 otherwise.

**Duplicate participation is prevented by the database.** A unique constraint on
`(activity_id, user_id)` means a repeated request cannot create a second row for the same
member, even when the service-level check is bypassed by two requests arriving together.

**All JPA associations are lazy.** `@ManyToOne` defaults to `EAGER`, which would pull the
sender, the activity and the activity's creator on every single message read. Every
association is explicitly `LAZY`, and `spring.jpa.open-in-view` is disabled so that lazy
loading cannot happen silently during response rendering.

**Errors are mapped to meaningful status codes.** A `@RestControllerAdvice` turns domain
exceptions into 404, 403 and 409 with a consistent JSON body, including field-level
messages for validation failures.

## Running locally

```bash
docker compose up -d       
./mvnw spring-boot:run     
```

Then open:

- `http://localhost:8080` — the web interface
- `http://localhost:8080/swagger-ui.html` — API documentation

The JWT signing key is read from the `JWT_SECRET` environment variable; the value in
`application.yml` is a local development placeholder.

## API

| Method | Path | Access |
|---|---|---|
| POST | `/api/auth/register` | public |
| POST | `/api/auth/login` | public |
| GET | `/api/activities` | public |
| GET | `/api/activities/{id}` | public |
| POST | `/api/activities` | authenticated |
| POST | `/api/activities/{id}/join` | authenticated |
| DELETE | `/api/activities/{id}/leave` | authenticated |
| GET | `/api/activities/{id}/messages` | participants only |
| POST | `/api/activities/{id}/messages` | participants only |
| GET | `/api/users/me` | authenticated |
| PUT | `/api/users/me` | authenticated |
| GET | `/api/users/{id}` | authenticated |

## What I would change next

- **Concurrent joins.** The capacity check reads the participant count and then inserts,
  so two requests racing for the last free slot can both pass. Locking the activity row
  for the duration of the transaction would close the gap; this needs a test that fires
  parallel requests and asserts the capacity is never exceeded.
- **Live chat.** The frontend polls the chat every five seconds; WebSocket would remove
  the polling.
- **Tests.** Integration tests against a real PostgreSQL instance via Testcontainers.
