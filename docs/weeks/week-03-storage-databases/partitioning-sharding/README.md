# Partitioning & Sharding

## 1. Partitioning vs Sharding

- **Partitioning** = dividing data into smaller subsets for easier management.
  - **Vertical partitioning**: splitting columns into different tables/services.  
    Example: `users` → move `bio, photo` into another table.  
  - **Horizontal partitioning (sharding)**: splitting rows into different groups.  
    Example: users A–M on one server, N–Z on another.

- **Sharding** = a specific form of horizontal partitioning where each group of data lives on a **separate node/database**.  
  The system needs a **shard key** to decide where each record goes.  

---

## 2. Basic Example
A system with 1M users in PostgreSQL starts sharding with:
```text
userId % 4
•   Shard0: users 0–249k
•	Shard1: users 250k–499k
•	Shard2: users 500k–749k
•	Shard3: users 750k–999k
```
Each shard now stores a subset, reducing load and size.

## 3. Pros and Cons
### Pros
- Horizontal scalability.
- Better performance (smaller dataset per shard).
- Allows commodity hardware instead of one giant DB.

### Cons
- Application complexity (where is each user?).
- Cross-shard joins are expensive.
- Re-sharding is painful if not using consistent hashing.

## 4. Gotchas
### Hot Spots
- Poor shard key → skewed traffic distribution.
- Example: shard by country, 80% of users are US.
- Fix: use hash(userId) or composite keys.

### Costly Re-sharding
With userId % n, if you go from 4 to 5 shards → all keys get remapped.
Fix: use consistent hashing.

### Cross-shard queries
Example: “Top 10 global users” → requires querying all shards and aggregating results in a coordinator.

## 5. Consistent Hashing
### Idea
- Represent the hash space as a ring (0°–360°).
- Servers (shards) are placed on the ring using a hash of their ID.
- Each key (e.g., userId) is also hashed and placed on the ring.
- Rule: each key is assigned to the first server clockwise.

### Example
```text
Servers:
	•	A → 90°
	•	B → 180°
	•	C → 270°
	•	D → 360°
	•	hash(user101) = 100° → assigned to B.
	•	hash(user205) = 260° → assigned to C.

If a new server E is added at 200°, only the keys between 180° and 200° move to E.
All others remain unchanged → huge improvement over % n.
```

## 6. Example in Scala
```scala
import java.security.MessageDigest

object ConsistentHashingDemo {
  def hashKey(key: String): Long = {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(key.getBytes)
    ((digest(0) & 0xFFL) << 24) | ((digest(1) & 0xFFL) << 16) |
    ((digest(2) & 0xFFL) << 8)  | (digest(3) & 0xFFL)
  }

  var servers: List[(String, Long)] = List(
    "ShardA" -> hashKey("ShardA"),
    "ShardB" -> hashKey("ShardB"),
    "ShardC" -> hashKey("ShardC")
  ).sortBy(_._2)

  def getShard(userId: String): String = {
    val h = hashKey(userId)
    servers.find(_._2 >= h).map(_._1).getOrElse(servers.head._1)
  }

  def main(args: Array[String]): Unit = {
    val user = "user12345"
    val shard = getShard(user)
    println(s"$user → $shard")
  }
}
```

## 7. Real-World Cases
### E-commerce
- Sharding by userId for carts and orders.
- Global reports → require cross-shard aggregation.

### Uber
- Sharding trips by regionId + tripId.
- Fast lookups for trips in the current region.

### Twitter
- Tweets sharded by userId.
- Timelines require knowing where followings are sharded.


## 8. Interview Exercise

**Question**: “You have 3 PostgreSQL shards, how would you implement consistent hashing so each userId is assigned to a shard?”

**Expected answer:**
- Build a hash ring.
- Place each shard on the ring.
- Hash each userId and map it to the first shard clockwise.
- Adding a new shard only remaps the keys between its predecessor and itself.

**Follow-up:**
- What happens if shard C fails?
    - Only the keys between B and C get redistributed.
- Why is this better than % n?
    - With % n, all keys get redistributed when n changes.
    - With consistent hashing, only a small fraction of keys move.

## 9. Key Takeaways
- Partitioning = splitting data; Sharding = distributing horizontally across nodes.
- Choosing a bad shard key leads to hot spots.
- Cross-shard queries are expensive and require coordination.
- Consistent hashing minimizes data movement when scaling shards.
- Used heavily in distributed databases (Cassandra, DynamoDB) and distributed caches (Memcached, Redis).