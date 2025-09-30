# Replication

## 1. Definition
Replication means **copying data across multiple nodes** to improve:
- Availability – if one node fails, another can serve requests.  
- Read scalability – distribute read traffic across replicas.  
- Disaster recovery – backup in other regions.  

The challenge is deciding **how and when** to replicate.

---

## 2. Leader–Follower (Master–Replica) Replication
- One node is the **Leader** → handles all writes.  
- Other nodes are **Followers** → replicate from the leader.  
- Reads can go to the leader or followers.  

**Example:**  
PostgreSQL on AWS RDS:  
- **Writer (Leader)** in `us-east-1`.  
- **Read Replicas** in `us-east-1b` and `us-east-2`.

Pattern: **1 leader → N replicas**.

---

## 3. Multi-Leader Replication
- Multiple leaders, each accepting writes.  
- Leaders replicate changes to each other.  
- Useful in **multi-region deployments**.  

**Example:**  
- US users write to leader in Virginia.  
- EU users write to leader in Frankfurt.  
- Both leaders sync with each other.

Problem: **conflicts** when the same record is updated in different regions.

---

## 4. Conflict Resolution

### Last Write Wins (LWW)
- Keep the version with the latest timestamp.  
- Risk of data loss if clocks are not synchronized.

### Vector Clocks
- Each replica tracks versions with a vector of counters.  
- Can detect conflicts and allow merges (app logic).  
- Used in Amazon DynamoDB.  

**Example:**  
User changes email in US and EU at the same time.  
- LWW → one change is discarded.  
- Vector clocks → both versions detected, app decides merge.

---

## 5. Synchronous vs Asynchronous Replication

### Synchronous
- Leader waits for replicas to confirm before acknowledging the client.  
- (Good) Strong consistency.  
- (Bad) Higher latency.  
- Example: **bank transfers**.

### Asynchronous
- Leader responds immediately, sends updates to replicas later.  
- (Good) Low latency, faster writes.  
- (Bad) Risk of data loss if leader crashes before replication.  
- Example: **analytics events**.

---

## 6. Practical Example
A payment system:  
- Main DB: PostgreSQL (Leader in `us-east-1`).  
- Replica in `us-west-1`.  
- Configured as **synchronous** to ensure payments exist in both regions before confirming.  

For **activity logs**:  
- Replica configured as **asynchronous**, since losing a log entry is acceptable.

---

## 7. Real-World Usage
- **MySQL/Postgres**: leader–follower replication.  
- **Cassandra/DynamoDB**: multi-leader, with conflict resolution.  
- **MongoDB**: replica sets with automatic failover.  

---

## 8. Interview Q&A

**Q1:** Explain leader-follower vs multi-leader replication.  
**A1:** Leader-follower has one leader for writes and multiple followers for reads. Multi-leader allows writes in multiple regions but requires conflict resolution.  

**Q2:** What is the difference between synchronous and asynchronous replication?  
**A2:** Synchronous waits for replicas → strong consistency, higher latency. Asynchronous returns faster but risks data loss.  

**Q3:** Which replication mode would you use for payments vs analytics?  
**A3:** Payments → synchronous (data integrity is critical). Analytics/logs → asynchronous (low latency, tolerate small data loss).  

---

## 9. Key Takeaways
- Replication improves **availability, scalability, and recovery**.  
- **Leader-follower** is simple, but only one write node.  
- **Multi-leader** enables geo-distributed writes, but conflicts arise.  
- **Synchronous** = strong consistency + higher latency.  
- **Asynchronous** = faster writes + risk of data loss.  