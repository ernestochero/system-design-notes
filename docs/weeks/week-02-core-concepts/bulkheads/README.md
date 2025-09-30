# Bulkhead Pattern

## 1. Definition
The **Bulkhead pattern** comes from ship design, where compartments are isolated so if one floods, the rest remain safe.  
In distributed systems, bulkheads isolate **resources (threads, connections, CPU, memory)** by function or service.  
This ensures that if one part of the system fails or becomes overloaded, it does not drag down everything else.

---

## 2. Without Bulkheads
A backend service handles:
- **Payments**
- **Notifications**
- **Reports**

All share the same **database connection pool**.  
If reports launch millions of heavy queries, they exhaust the pool → payments and notifications also fail.

---

## 3. With Bulkheads
Resources are separated:
- Dedicated pool for **Payments** (high priority).
- Separate pool for **Notifications**.
- Limited pool for **Reports** (low priority).

Even if reports overload their pool, **Payments continue working**.

---

## 4. Real-World Cases

### Payments vs. Reporting
- Stripe: isolate payment processing (critical) from report generation (non-critical).

### Streaming
- Netflix: playback service is isolated from recommendation service.  
- If recommendations fail, playback remains unaffected.

### Airline Systems
- Flight **reservations** use a critical pool.  
- Flight **search queries** use another pool.  
- Heavy searches won’t block reservations.

---

## 5. Implementation Strategies
- **Separate thread pools** per request type or service.
- **Independent connection pools** for different microservices.
- **Dedicated queues/buffers** to prevent one queue from blocking others.
- **Per-endpoint rate limiting** to isolate load.

---

## 6. Example (Scala + Futures)

```scala
import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}

// Separate thread pools (bulkheads)
val paymentsEc = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(10))
val reportsEc  = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(3))

def processPayment(): Future[String] = Future {
  // critical work
  "Payment processed"
}(paymentsEc)

def generateReport(): Future[String] = Future {
  // heavy work
  "Report generated"
}(reportsEc)
```