# Week 3 – Storage & Databases

This week focuses on understanding **databases, storage engines, caching, and partitioning** — the foundation of any large-scale system.  

---

## Topics

### 1. Database Types
- **Relational (SQL)**: PostgreSQL, MySQL, Aurora  
- **NoSQL**: 
  - Document (MongoDB)  
  - Key-Value (DynamoDB, Redis)  
  - Wide-Column (Cassandra)  
  - Graph (Neo4j)  
- Trade-offs: consistency vs flexibility, schema vs schema-less.

_“SQL databases enforce schema and strong consistency, which ensures data integrity but makes evolution harder. NoSQL databases are schema-less and more flexible, which accelerates development and handles varied data, but may sacrifice consistency and require the application to enforce rules.”_

---

### 2. Partitioning & Sharding
- Horizontal vs vertical partitioning.  
- Choosing a good shard key.  
- Rebalancing shards and handling hot spots.  
- Problems with resharding and consistency across shards.

---

### 3. Replication
- **Leader–Follower (Master–Replica)** replication.  
- Multi-leader replication across regions.  
- Conflict resolution (last write wins, vector clocks).  
- Synchronous vs asynchronous replication.

---

### 4. Indexing
- How indexes speed up queries.  
- B-Tree vs Hash indexes.  
- Composite indexes and covering indexes.  
- Trade-offs: write cost vs read performance.

---

### 5. Caching
- Cache strategies: write-through, write-back, write-around.  
- Cache invalidation techniques.  
- App-level caches (Redis, Memcached) vs CDN caches (CloudFront, Akamai).  

---

### 6. Storage Engines & Log-Structured Systems
- How relational DBs like Postgres store data (heap, WAL logs).  
- LSM Trees (Cassandra, RocksDB, LevelDB).  
- Trade-offs between B-Tree vs LSM.  

---

## Exercises

1. **Design a Twitter Timeline**  
   - How would you store tweets? Which DB type?  
   - How do you scale to billions of reads with caching?  

2. **Design a Flight Booking System**  
   - How do you shard data by region?  
   - What replication model ensures availability + consistency?  

3. **Design a Search Feature**  
   - How do you index text data for fast queries?  
   - What trade-offs exist between B-Tree and inverted indexes?  

---

## Key Takeaways
- Choosing the right **database type** is fundamental.  
- **Partitioning and replication** drive scalability and availability.  
- **Indexes and caching** optimize for performance.  
- Understanding **storage engines** gives insight into DB trade-offs.