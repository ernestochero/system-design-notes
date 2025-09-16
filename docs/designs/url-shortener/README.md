# URL Shortener (Bitly) – HLD

> Template: `templates/DESIGN_TEMPLATE.md`

## Requirements (summary)
- Shorten URLs, redirect in <30ms p95, 99.9% availability
- Custom alias, click analytics
- 1B links, 50k RPS peak reads

## Architecture (sketch)
```mermaid
flowchart LR
  U[User] --> API
  API --> WR[Write Svc]
  API --> RD[Read Svc]
  WR --> IDG[ID Generator (Snowflake/KSUID)]
  WR --> DB[(KV Store / Column Family)]
  RD --> Cache[(Redis)]
  RD --> DB
  WR --> MQ[[Kafka]] --> ANA[Analytics Pipeline]
```

## Trade-offs
- Strong vs eventual consistency for reads
- Hot keys & cache stampede control
- Multi-DC replication & geo-routing
