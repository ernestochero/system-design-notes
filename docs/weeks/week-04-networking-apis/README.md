# Week 04 – Networking & APIs

This week we studied the fundamentals of **networking and API design** for distributed systems.  
The focus is on understanding how communication flows between services, how to balance traffic, design efficient and secure APIs, and manage messaging/event-driven patterns.

---

## Key Topics

### 1. Networking Basics
- Differences between **HTTP/1.1, HTTP/2, HTTP/3 (QUIC)**.
- TCP vs UDP: when to use each.
- TLS handshake & mutual TLS (mTLS).
- Connection pooling and its impact on performance.

### 2. Load Balancing
- **L4 (TCP) vs L7 (HTTP) load balancers**.
- Load balancing algorithms: round robin, least connections, consistent hashing.
- **Global vs local load balancing** (e.g., AWS Route53, Cloudflare).
- Health checks and automatic failover.

### 3. API Design
- Comparison: **REST vs gRPC vs GraphQL**.
- API Gateway patterns.
- Rate limiting and throttling.
- API versioning and backward compatibility.

### 4. Messaging & Event-Driven
- **Pub/Sub** with Kafka, SNS/SQS, RabbitMQ.
- Delivery semantics: at-most-once, at-least-once, exactly-once.
- Eventual consistency in event-driven architectures.
- Dead-letter queues.

### 5. Security
- **OAuth2, JWT, API keys**.
- mTLS & certificate rotation.
- DoS protection with rate limiting.
- Data encryption in transit vs at rest.

---

## Goal of the Week
By the end of this week, you should be able to:
1. Explain key differences between HTTP protocols and TCP/UDP transport.  
2. Design a **load balancing** strategy with failover.  
3. Choose between REST, gRPC, or GraphQL depending on the use case.  
4. Implement an **event-driven system** with delivery guarantees.  
5. Secure your APIs with **OAuth2, JWT, and mTLS**.  