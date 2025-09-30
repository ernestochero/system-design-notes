# Circuit Breaker Pattern

## 1. Why Circuit Breakers?
In distributed systems, a failing dependency can trigger a **cascade of failures**:
- Clients keep sending requests.
- Each request times out, consuming threads, sockets, CPU.
- Soon, the entire system is overloaded.

A **Circuit Breaker** protects the system by cutting off calls to the failing dependency after a threshold is reached.

---

## 2. States of a Circuit Breaker

1. **Closed**: Normal operation, requests flow through.
   - If failures exceed a threshold (e.g., 50% of calls fail), breaker trips → goes to *Open*.

2. **Open**: All requests fail fast (error returned immediately).
   - Protects system from wasting resources on doomed calls.
   - After a timeout period, moves to *Half-Open*.

3. **Half-Open**: Allows a limited number of test requests.
   - If they succeed → back to *Closed*.
   - If they fail → back to *Open*.

---

## 3. Real-World Cases

### Netflix (Hystrix OSS)
- Problem: A microservice calling another slow service caused thread pools to saturate.
- Solution: **Hystrix circuit breakers** → fail fast instead of blocking.
- Bonus: fallback logic (return cached/empty response) kept the UI responsive.

---

### Payment Gateway
- Problem: External provider goes down.
- Without breaker: clients retry aggressively → gateway collapses even after recovery.
- With breaker: calls fail fast, retries are limited, system stays healthy until provider recovers.

---

### Airline Search
- Problem: Flight search aggregates 10+ providers.
- If one provider is slow, all queries hang.
- With breaker: failing provider is skipped → rest of results still load.

---

## 4. Design Considerations

- **Failure threshold**: e.g., trip breaker if >50% failures in last 20 requests.
- **Timeout window**: how long before moving from *Open* → *Half-Open*.
- **Fallbacks**:
  - Return cached data.
  - Show degraded service (partial results).
  - Return default error gracefully.

---

## 5. Pseudo-code Example

```scala
class CircuitBreaker(maxFailures: Int, resetTimeout: FiniteDuration) {
  private var failureCount = 0
  private var state: State = Closed
  private var lastFailureTime: Long = 0

  def call[T](operation: => T): Try[T] = {
    state match {
      case Open if (System.currentTimeMillis - lastFailureTime) < resetTimeout.toMillis =>
        return Failure(new RuntimeException("Circuit is Open"))

      case Open =>
        state = HalfOpen

      case _ =>
    }

    try {
      val result = operation
      onSuccess()
      Success(result)
    } catch {
      case e: Exception =>
        onFailure()
        Failure(e)
    }
  }

  private def onSuccess(): Unit = {
    failureCount = 0
    state = Closed
  }

  private def onFailure(): Unit = {
    failureCount += 1
    lastFailureTime = System.currentTimeMillis
    if (failureCount >= maxFailures) state = Open
  }
}
```