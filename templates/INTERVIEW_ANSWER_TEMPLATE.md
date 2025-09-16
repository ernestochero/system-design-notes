# Question
…

# 1-Min Answer
…

# Deep Dive Talking Points
- …
- …
- …

# Diagram
```mermaid
sequenceDiagram
  participant C as Client
  participant A as API
  participant S as Service
  participant D as DB
  C->>A: Request
  A->>S: Validate & Route
  S->>D: Read/Write
  D-->>S: Result
  S-->>A: DTO
  A-->>C: Response
```
