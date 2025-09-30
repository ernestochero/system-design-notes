# Consistency Models

## 1. Strong Consistency
- Every read returns the result of the **most recent write**.
- From the client’s perspective, it looks like there is a single up-to-date copy of the data.
- **Examples**: Zookeeper, etcd, Google Spanner.
- ✔️ Easy to reason about, ❌ higher latency due to coordination.

---

## 2. Eventual Consistency
- Updates propagate asynchronously, and replicas **converge eventually**.
- If no new updates are made, all nodes will eventually return the same value.
- **Examples**: DynamoDB, Cassandra (default mode), DNS.
- ✔️ High availability, ❌ clients may see stale data.

---

## 3. Causal Consistency
- If operation **A happens-before B**, then every client will see A before B.
- Does not guarantee global ordering, but preserves cause-effect relationships.
- **Examples**: Social networks (a “like” should not appear before the related post).

---

## 4. Read-Your-Writes (RYW)
- A client always sees **its own updates** immediately.
- Other clients might still see stale data, but you never read an outdated version of your own write.
- **Examples**: Updating your profile picture → you should see it right away.

---

## 5. Monotonic Reads
- Once a client has seen a newer value, it will **never see an older value again**.
- Prevents “going back in time” with reads.
- **Examples**: Log systems or timelines where entries should not disappear.

---

## Comparison Table

| Model                | Guarantee                          | Typical Use Case                 |
|----------------------|------------------------------------|----------------------------------|
| **Strong**           | Always latest value                | Banking, payments                |
| **Eventual**         | Converges eventually               | Product catalogs, DNS            |
| **Causal**           | Preserves cause-effect ordering    | Likes after posts in social apps |
| **Read-Your-Writes** | Client sees own updates immediately| User profile updates             |
| **Monotonic Reads**  | Never reads older value after newer| Logs, timelines                  |

---

## Exercises

1. **Design a social network:**
   - Which model for **likes**? Causal Consistency.
   - Which model for **your own post** right after publishing? Read-Your-Writes.

2. **Design a payment system:**
   - Which model for **account balances**? Strong Consistency.
   - Which model for **transaction history in mobile app**? Monotonic Reads.