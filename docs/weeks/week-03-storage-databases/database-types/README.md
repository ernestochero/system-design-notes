# Database Types

## 1. Overview
Databases are not one-size-fits-all. Modern systems use **polyglot persistence**: different database types for different workloads.  
Here’s a quick overview:

- **Relational (SQL)** – PostgreSQL, MySQL, Aurora  
- **Document** – MongoDB, Couchbase  
- **Key-Value** – DynamoDB, Redis, FoundationDB  
- **Wide-Column** – Cassandra, HBase, Bigtable  
- **Time-Series** – InfluxDB, TimescaleDB, QuestDB  
- **Graph** – Neo4j, JanusGraph  
- **Columnar (OLAP)** – BigQuery, Snowflake, ClickHouse  
- **Search Engines** – Elasticsearch, OpenSearch  

---

## 2. When to Use Each
- **SQL** → Transactions, joins, ad-hoc queries.  
- **Document** → Flexible schema, product views.  
- **Key-Value** → Ultra-fast lookups by key.  
- **Wide-Column** → Huge scale, timelines, IoT, logs.  
- **Time-Series** → Metrics, telemetry, downsampling.  
- **Graph** → Social graphs, fraud detection, dependency graphs.  
- **Columnar** → Analytics, BI, large scans.  
- **Search** → Text search, relevance, autocomplete, faceting.  

---

## 3. Complex Real-World Examples

### E-commerce (Amazon-like)
- **Orders/Payments** → SQL (ACID).  
- **Catalog** → Document DB (flexible schema).  
- **Search** → Elasticsearch (text, filters).  
- **Cart/Sessions** → Redis (fast, TTL).  
- **Analytics** → BigQuery/Snowflake.  

Trade-off: Catalog duplicated in Document + ES → need pipelines for consistency.

---

### Ride-Sharing (Uber-like)
- **Users/Drivers/Trips** → SQL.  
- **Telemetry** → Time-Series DB (millions of writes/sec).  
- **Geo-Indexing** → Wide-Column + S2 cells.  
- **Ledger (Payments)** → SQL (CP).  
- **Feed/State updates** → Redis pub/sub.  

Trade-off: Geo in PostGIS rich but scales worse than S2 + Cassandra.

---

### Social Network (Twitter/Facebook-like)
- **Relationships** → Graph DB (Neo4j) or adjacency lists in SQL.  
- **Feeds** → Wide-Column (Cassandra timelines).  
- **Search** → Elasticsearch for text.  
- **Profiles** → SQL.  

Trade-off: Graph DB great for deep queries, bad for high-volume feeds → use wide-column.

---

## 4. Gotchas

### Hot Partitions
- Bad shard key → all traffic on one partition.  
- Example: `country=US` in DynamoDB → one shard explodes.  
- Fix: Hash + composite key.

### Over-Duplication in Document Stores
- Embedding too much data → massive updates needed.  
- Example: duplicating user email in every order.  
- Fix: only embed frozen fields (e.g., price at purchase).

### LSM Write Amplification
- SSTables need compactions.  
- Example: IoT metrics → 10 SSTables checked per read.  
- Fix: choose correct compaction strategy.

### Global 2PC Transactions
- Cross-region transactions break on failures.  
- Example: booking flights with DBs in USA + EU.  
- Fix: use sagas/eventual consistency.

### Secondary Index Pitfalls
- In Cassandra, secondary indexes are local, not global.  
- Querying `status=PENDING` scans all nodes.  
- Fix: model tables per query (e.g., `transactions_by_status`).

### One DB to Rule Them All
- Using one DB for everything → poor performance.  
- Example: Postgres for catalog + search → slow full-text queries.  
- Fix: polyglot persistence (SQL + ES + Redis + OLAP).  

---

## 5. SSTables (Sorted String Tables)

### What is an SSTable?
- Immutable, sorted file on disk.  
- Used by LSM Tree DBs (Cassandra, Bigtable, RocksDB).  
- Stores key-value pairs in sorted order.

### Write Path
1. Write to **commit log** (durability).  
2. Store in **MemTable (RAM)**.  
3. When MemTable is full → flush to **SSTable**.

### Read Path
- Check MemTable + Bloom filters + SSTables.  
- May need to merge results from multiple SSTables.  
- Compaction merges SSTables into fewer files, keeping latest values.

### Example
1. Write `user:1 → "Ernesto"` → goes to MemTable.  
2. Flush → SSTable1.  
3. Update `user:1 → "Neto"` → goes to SSTable2.  
4. Read → DB sees SSTable2 (latest).  
5. Compaction → merge into one SSTable: `"Neto"`.

### Pros
- Fast writes (append-only).  
- Immutable → replication is simple.  

### Cons
- Reads may hit many SSTables.  
- Compaction is expensive.  
- Deletes leave tombstones until compaction.  

---

## 6. Interview Exercises
1. **Design a global e-commerce backend**  
   - Which DB for orders, catalog, search, analytics?  
   - How do you keep ES/Redis consistent with SQL?
   - **Solution:** I’d use SQL for critical flows, Document + ES for flexible catalog/search, Redis for sessions, and a columnar OLAP for analytics. This ensures each workload uses the best-suited database.

2. **Trip store for ride-sharing**  
   - Model trips, telemetry, surge pricing.  
   - Which DB for each, and why?
   - **Solution:** I’d separate trip metadata in SQL from high-volume GPS in a time-series DB, use Cassandra for geo cells, Redis for surge pricing, and SQL for the payment ledger. This balances consistency with scale.

3. **Social network feed**  
   - Fan-out on write vs fan-out on read.  
   - How do you combine Cassandra + ES + Graph DB?
   - **Solution:** I’d model relationships in a Graph DB for deep traversals, but timelines in Cassandra with fan-out on write for fast reads. Elasticsearch handles search, while SQL keeps consistent user profiles
   - In fan-out on write, each new post is immediately copied into the followers’ timelines, which makes reads fast but writes expensive. In fan-out on read, posts are stored once and combined when a user opens their timeline, which makes writes cheap but reads expensive. Real systems like Twitter use a hybrid: fan-out on write for normal users and fan-out on read for celebrities.

---

## 7. Key Takeaways
- Use **SQL for consistency**, **NoSQL for scale**, **Search for text**, **OLAP for analytics**.  
- Avoid hot partitions, over-duplication, and secondary index traps.  
- Understand **SSTables** to reason about LSM-based DB performance.  
- Embrace **polyglot persistence** for real-world systems.