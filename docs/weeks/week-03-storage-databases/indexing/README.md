

# Indexing

## 1. How Indexes Speed Up Queries
Indexes are like the index of a book:  
- Without an index → you read every page to find something (**full table scan**).  
- With an index → you jump directly to the right page.  

Example:
```sql
SELECT * FROM users WHERE email = 'test@example.com';
```
- Without index → scan 1M rows.  
- With index on `email` → jump directly to the row.

---

## 2. Types of Indexes

### 🔹 B-Tree Index
- Default in most RDBMS (PostgreSQL, MySQL, Oracle).
- Stores data in a balanced tree structure.
- Supports:
  - Equality (`=`)
  - Range queries (`<`, `>`, `BETWEEN`)
  - Sorting (`ORDER BY`)

**Example:**  
```sql
SELECT * FROM users WHERE age BETWEEN 20 AND 30;
```
→ B-Tree only traverses the range, much faster.

---

### 🔹 Hash Index
- Uses a hash table internally.
- Only supports **equality lookups** (`=`).
- Very fast for exact matches, but no range queries.

**Example:**  
```sql
SELECT * FROM users WHERE user_id = 123;
```
→ Hash index outperforms B-Tree here.

---

## 3. Composite Indexes
Indexes on multiple columns.  
```sql
CREATE INDEX idx_users_name_age ON users (last_name, first_name);
```

Query:
```sql
SELECT * FROM users 
WHERE last_name = 'Smith' AND first_name = 'John';
```
→ Uses the composite index.

Order matters: `(last_name, first_name)` works for `last_name` or `last_name + first_name`, but **not** for `first_name` alone.

---

## 4. Covering Indexes
A **covering index** includes all columns required for a query, so the query can be resolved **entirely from the index** without reading the table.

### Example 1: Without covering index
```sql
CREATE INDEX idx_users_email ON users (email);

SELECT name, created_at 
FROM users 
WHERE email = 'test@example.com';
```
- Index finds the email quickly.  
- But DB must still access the table to fetch `name` and `created_at`.

---

### Example 2: With covering index
```sql
CREATE INDEX idx_users_email_cover 
ON users (email) INCLUDE (name, created_at);

SELECT name, created_at 
FROM users 
WHERE email = 'test@example.com';
```
- Index already contains `email, name, created_at`.  
- Query resolved **entirely from index**.  

---

### Example 3: Composite + Covering
```sql
CREATE INDEX idx_orders_user_date 
ON orders (user_id, order_date) 
INCLUDE (amount, status);

SELECT order_date, amount, status
FROM orders
WHERE user_id = 123;
```
- Index filters by `user_id`.  
- Already has `amount` and `status`.  
- No need to touch the table.

---

### Supported Databases
- **PostgreSQL** → since v11 with `INCLUDE`.  
- **SQL Server** → supports “included columns”.  
- **MySQL/MariaDB (InnoDB)** → clustered indexes + secondary indexes can act as covering.  
- **Oracle** → composite/functional indexes provide similar behavior.  
- **SQLite** → naturally supports covering indexes.

---

## 5. Trade-offs: Write Cost vs Read Performance
Indexes drastically speed up **read queries**.  
Indexes slow down **writes**: every `INSERT`, `UPDATE`, `DELETE` must update indexes.  

**Example:**  
`transactions` table with 100M rows.  
- Indexes on `(user_id)`, `(date)`, `(amount)` → reads are fast.  
- But each new transaction updates 3 indexes → slower writes.

Rule of thumb:
- **Read-heavy systems (dashboards, reporting)** → more indexes.  
- **Write-heavy systems (payments, logging)** → fewer, carefully chosen indexes.

---

## 6. Real-World Usage
- E-commerce: index `product_id`, `category`, `created_at`.  
- Banking: index `account_id` for transactions.  
- Social media: index `(user_id, created_at)` for feeds.  

---

## 7. Interview Q&A

**Q1:** How do indexes speed up queries?  
**A1:** By avoiding full table scans and allowing direct access to rows using efficient data structures (B-Tree, Hash).  

**Q2:** Difference between B-Tree and Hash indexes?  
**A2:** B-Tree supports equality, ranges, and sorting; Hash is optimized for equality only.  

**Q3:** What is a covering index?  
**A3:** An index that contains all columns required for a query, eliminating the need to access the base table.  

**Q4:** Trade-offs of indexing?  
**A4:** Indexes improve read performance but slow down writes, as indexes must be updated for each modification.  

---

## 8. Key Takeaways
- Indexes = faster reads, but costlier writes.  
- **B-Tree** = general-purpose, supports ranges.  
- **Hash** = faster equality lookups, but limited.  
- **Composite indexes** = useful for multi-column queries (order matters).  
- **Covering indexes** = queries served directly from index, widely supported in modern databases.  