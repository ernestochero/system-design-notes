# Idempotency

## 1. Definition
An operation is **idempotent** if executing it once or multiple times produces the **same result**.  
Critical in distributed systems where retries are common (due to network issues, timeouts, or client replays).

---

## 2. Simple Examples
- **Idempotent**:
  - `GET /user/123` → always returns the same user.
  - `DELETE /user/123` → deleting multiple times leads to the same state (user no longer exists).
- **Non-idempotent**:
  - `POST /payment` → each request could create a new charge (risk of duplicates).

---

## 3. Real-World Case: Payments
- **Problem**: A client pays $100 but loses connection. They retry → could be charged twice.
- **Solution**:
  - Introduce an **idempotency key** (e.g., `paymentRequestId`).
  - All retries with the same key return the **same response**, not a new charge.
- **Industry practice**: Stripe, PayPal, Adyen use `Idempotency-Key` header in HTTP APIs.

---

## 4. Real-World Case: Reservations
- **Problem**: Double retries when reserving a seat could book it twice.
- **Solution**:
  - Associate the operation with a **unique requestId**.
  - If the seat is already reserved for that request → ignore duplicate attempts.

---

## 5. Common Implementation
Maintain a **request log table** in DB:
- `idempotencyKey`
- `requestPayload`
- `responsePayload`

Process flow:
1. Look up the `idempotencyKey`.
2. If found → return cached response.
3. If not → execute operation and save the result.

---

## 6. Pseudo-code (Scala)

```scala
def processPayment(request: PaymentRequest, key: String): PaymentResponse = {
  db.findByIdempotencyKey(key) match {
    case Some(existing) =>
      // Return cached result
      existing.response
    case None =>
      val response = chargeCard(request)
      db.save(IdempotencyRecord(key, request, response))
      response
  }
}
```