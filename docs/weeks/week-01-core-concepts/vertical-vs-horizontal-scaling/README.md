

# Vertical vs Horizontal Scaling

## Vertical Scaling (Scale Up)
- Upgrade a single machine with more CPU, RAM, faster storage.
- **Pros**
  - Simple to implement (minimal architecture changes).
  - Good first step when hitting small capacity issues.
- **Cons**
  - Hardware has physical limits.
  - Costs increase quickly as machines get bigger.
  - Single point of failure remains.

**Example:**  
A single database server starts lagging → upgrade from 16 GB RAM to 128 GB RAM and add SSDs. Performance improves without changing the application.

---

## Horizontal Scaling (Scale Out)
- Add more machines/instances and distribute traffic.
- **Pros**
  - Near-infinite scalability by adding more nodes.
  - Provides redundancy and fault tolerance.
  - Commodity hardware is often cheaper.
- **Cons**
  - Architecture complexity increases.
  - Requires load balancing, data partitioning/sharding, and coordination.
  - Harder to guarantee strong consistency.

**Example:**  
An API grows to 10k RPS → instead of one big server, deploy 20 smaller servers behind a load balancer. Requests are distributed evenly.

---

## Comparison

| Aspect              | Vertical Scaling (Up)      | Horizontal Scaling (Out)        |
|---------------------|----------------------------|---------------------------------|
| Complexity          | Low                        | High                            |
| Growth Limit        | Hardware capped            | Near-infinite                   |
| Fault Tolerance     | None (single node failure) | High (redundancy)               |
| Cost                | Expensive for big servers  | Cheaper with commodity servers  |

---

## Exercise
If your database starts to slow down under load:
1. **Step 1:** Scale vertically → add CPU/RAM for a quick fix.
2. **Step 2:** When limits are reached → switch to horizontal scaling (read replicas, sharding).