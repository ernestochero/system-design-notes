# CAP Theorem

## What is CAP?
The **CAP theorem** (Brewer’s theorem) states that in a distributed system you can only guarantee **two out of three properties**:

1. **Consistency (C)** – all reads return the latest write (everyone sees the same data).
2. **Availability (A)** – every request receives a response, even if not the latest version.
3. **Partition Tolerance (P)** – the system continues working even if network failures split the cluster.

---

## The Practical Choice
In real-world distributed systems, **partitions (P)** are inevitable.  
Therefore, you must choose between:
- **CP (Consistency + Partition Tolerance)** → sacrifice Availability.
- **AP (Availability + Partition Tolerance)** → sacrifice Consistency.

---

## CP Example (Consistency + Partition Tolerance)
- Databases with quorum/synchronous writes (e.g., PostgreSQL with sync replication).
- If a node is down, the system may reject the write → consistent but not always available.
- Example: a **banking system** prefers to reject a transaction than show an incorrect balance.

---

## AP Example (Availability + Partition Tolerance)
- DynamoDB, Cassandra, Riak.
- If one replica is down, another responds → system remains available, but data may be stale.
- Example: a **social media feed** prefers to show slightly outdated posts rather than fail.

---

## Comparison Table

| Model | Guarantees                 | Sacrifices   | Typical Use Cases          |
|-------|----------------------------|--------------|----------------------------|
| **CP** | Consistency + Partition   | Availability | Banking, inventory systems |
| **AP** | Availability + Partition  | Consistency  | Catalogs, social networks  |

---

## Exercises

1. **Airline Reservation System**  
   Would you choose CP or AP?  
   👉 **CP** – better to reject a booking than sell the same seat twice.

2. **Social Media Feed**  
   Would you choose CP or AP?  
   👉 **AP** – better to always return something, even if slightly outdated.