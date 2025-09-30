# Quorum Reads & Writes

## 1. What is a Quorum?
In a distributed system with **N replicas**, a quorum means that an operation must be acknowledged by at least **⌈N/2 + 1⌉** nodes.  
This guarantees overlap between read and write sets → avoiding stale reads.

---

## 2. General Rule
For a cluster with **N replicas**:
- **W** = number of replicas that must confirm a **write**.
- **R** = number of replicas that must confirm a **read**.

Rule:  
**R + W > N → guarantees strong consistency**

---

## 3. Example (N = 3)
- Replicas: A, B, C
- Config: **W = 2, R = 2**
  - A write must be confirmed by 2 replicas.
  - A read queries 2 replicas and returns the latest version.
- Since **R + W = 4 > N = 3**, there is always an intersection ensuring consistency.

---

## 4. Trade-offs

| Config          | Meaning                                | Pros                      | Cons                  |
|-----------------|----------------------------------------|---------------------------|-----------------------|
| W=1, R=N        | Write to 1, read from all              | Strong reads              | Slow reads            |
| W=N, R=1        | Write to all, read from 1              | Strong writes             | Slow writes           |
| W=⌈N/2⌉, R=⌈N/2⌉ | Balanced approach                      | Consistency + availability| Higher latency        |

---

## 5. Real-World Cases

### Payments (CP-oriented)
- Config: **W=N, R=1**
- Every replica must confirm writes before success.
- Example: a financial ledger → cannot lose a transaction.

---

### Shopping Carts (AP-oriented)
- Config: **W=1, R=N** or even **W=1, R=1** (eventual consistency).
- Availability prioritized → carts can converge later.
- Example: Amazon shopping cart → must always be available.

---

### Product Catalog (Balanced)
- Config: **W=2, R=2 (N=3)**
- Ensures no stale price reads, but avoids waiting for all replicas.

---

## 6. Interview Exercises

1. **Cassandra with N=5**  
   - Which W and R ensure strong consistency?  
     Any values where R+W > 5 (e.g., W=3, R=3).

2. **Airline Reservations**  
   - Which config is best?  
    **CP-oriented (W high, R low)** → it’s worse to sell the same seat twice than to reject a booking.

---

## 7. Key Takeaways
- Quorums balance **consistency vs availability**.  
- Rule of thumb: **R + W > N → strong consistency**.  
- Choose config based on business trade-offs:
  - Payments → CP  
  - Shopping carts → AP  
  - Catalogs → Balanced