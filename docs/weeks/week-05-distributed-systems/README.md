# Week 05 — Distributed Systems & Scalability

## Objectives
Understand the principles and trade-offs behind building **scalable, fault-tolerant distributed systems**.  
This week explores the mechanisms that enable global-scale services like **Disney+, Netflix, or Amazon** to ensure reliability, availability, and consistency under heavy load.

---

## Topics Overview

### Consistency, Availability & Fault Tolerance
- Deep dive into the **CAP theorem** and its real-world implications.  
- Differences between **strong consistency** vs **eventual consistency**.  
- Practical patterns for resilience and fault recovery (retries, backoff, replication).  
- Example: How some real world projects ensures availability even during data center failures.

---

### Replication Strategies
- **Synchronous vs Asynchronous replication** and how they affect latency.  
- **Geo-replication** for multi-region systems.  
- **Conflict resolution** mechanisms: Vector clocks, CRDTs, and Last-Write-Wins (LWW).  
- Trade-offs between durability and availability in replicated stores.

---

### Leader Election & Coordination
- **Consensus algorithms**: Raft, Paxos, and how they maintain agreement across nodes.  
- Leader election in distributed clusters (Zookeeper, etcd).  
- Handling **split-brain** and quorum-based coordination.  

---

### Queues, Streams & Backpressure
- Differences between **message queues** (SQS, RabbitMQ) and **streaming platforms** (Kafka, Kinesis).  
- Managing high throughput and **backpressure** in streaming consumers.  
- **FS2 and Akka Streams** examples for reactive pipelines.

---

### Distributed Transactions & Sagas
- Why **two-phase commit (2PC)** is not scalable.  
- **Sagas** as a pattern for distributed consistency with compensating actions.  
- Idempotency and deduplication in event-driven architectures.  
- Example: Handling product updates or rollbacks across multiple services.

---

### Observability, Telemetry & Tracing
- How to trace requests end-to-end in distributed systems.  
- **Metrics, logs, and traces** as pillars of observability.  
- Tools: **OpenTelemetry, Jaeger, Datadog, Prometheus**.  
- Example: Monitoring latency spikes in real world ingestion pipelines.

---

## Real-World Relevance
- **Consistency**: Media states must remain synchronized across Disney+ and Hulu.  
- **Availability**: APIs must remain operational even if a region goes down.  
- **Scalability**:  Handles millions of content-state updates daily.  
- **Observability**: Every payment transaction is traceable through logs and telemetry.

---

## Key Takeaways
- Trade-offs are inevitable: **CAP theorem defines your design boundaries.**  
- **Replication and consensus** are the backbone of reliability.  
- **Streaming systems** enable real-time visibility and event-driven design.  
- **Observability** ensures transparency and trust in distributed operations.

---