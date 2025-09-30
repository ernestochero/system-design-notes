# Retries, Backoff & Jitter

## 1. Why Retries Matter
In distributed systems, transient failures are normal:
- Network packet loss
- Temporary overload
- Dependency hiccups (GC pause, restart, slow I/O)

A retry can mask a transient failure **if done correctly**.  
But a bad retry policy can **amplify an outage** (all clients retry at once).

---

## 2. Retry Strategies

### Naive Retry
- Immediately retry on failure.
- Risk: "retry storm" → thousands of clients hammering a struggling service.

### Exponential Backoff
- Each retry waits longer:
  - 100ms → 200ms → 400ms → 800ms…
- Reduces pressure on the failing service.

### Backoff + Jitter
- Add randomness to avoid synchronized retries.
- Example: instead of exactly 400ms, wait a random value between 300–500ms.
- Prevents the **thundering herd problem**.

---

## 3. Trade-offs

- **Too aggressive retries** → overload dependencies.  
- **Too conservative retries** → higher user-facing latency.  
- Solution: use **retry budgets** (limit total retry attempts per user/request).

---

## 4. Design Pattern (Pseudo-code)

```scala
def callWithRetry[T](operation: => T, maxRetries: Int = 3): Try[T] = {
  var attempt = 0
  var delay = 100 // ms

  while (attempt < maxRetries) {
    try {
      return Success(operation)
    } catch {
      case e: Exception =>
        attempt += 1
        if (attempt == maxRetries) return Failure(e)

        // Add jitter
        val jitter = scala.util.Random.between(-delay/2, delay/2)
        Thread.sleep(delay + jitter)

        delay *= 2 // exponential backoff
    }
  }
  Failure(new RuntimeException("unreachable"))
}
```

## 5. Interview Exercises
Payment API
- **You integrate with an external payment provider.**
- How do you prevent retries from charging a customer twice?
- Use idempotency keys + retries with jitter.

Booking System
- **Multiple clients retry when booking the last available seat.**
- How do you avoid overloading the DB?
- Apply exponential backoff, cap max retries, and use a queue/broker to smooth traffic.

## 6. Key Takeaways
- Always combine exponential backoff + jitter.
- Protect dependencies with retry budgets.
- Pair with idempotency keys for safety in financial workflows.
- Monitor retry rates → spikes often indicate hidden failures.

## 7. Real-World Case

### E-commerce (Shopping Cart on Black Friday)
- Thousands of users confirm payments simultaneously.  
- Without control, retries overload the payment gateway.  
- **Solution**: Backoff + jitter to spread retries, message queues to smooth spikes, and **idempotency keys** to avoid double charges.

### Airline Reservations (Global Traffic)
- Two users in different continents book the last seat.  
- A network partition makes the DB slow → both retry at once.  
- **Solution**: Exponential backoff + retry budgets, combined with **strong consistency locks** to prevent double booking.


### Streaming (Netflix/Disney+ DRM Service)
- Clients request a DRM token to start playback.  
- If the DRM service fails, millions retry → cascade failure.  
- **Solution**: Exponential backoff + jitter on clients, **circuit breakers** in services, and fallbacks (e.g., temporary offline mode).


### Payment Processing at Scale (Batch B2B)
- A company uploads 1M transactions, provider limits at 100 RPS.  
- Naive retries → massive throttling and timeouts.  
- **Solution**: Retry with backoff, client-side **rate limiting**, and queue-based throttling.


### Mobility Apps (Uber/Cabify)
- Clients poll driver locations every few seconds.  
- If the location service fails, all clients retry at once.  
- **Solution**: Retry with jitter, cache last known position, and switch from **pull → push** (WebSockets) to reduce retry storms.


### Lessons
- Retries without control **make outages worse**.  
- Combine **Backoff + Jitter** with:
  - **Idempotency** (prevent duplicates)  
  - **Circuit breakers** (stop storms)  
  - **Rate limiting** (protect downstreams)  
  - **Queues/events** (absorb spikes)