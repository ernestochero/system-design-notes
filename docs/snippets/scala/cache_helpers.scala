package notes

object CacheHelpers {
  // Basic cache-aside pattern sketch
  def getOrLoad[K, V](key: K)(load: => V)(put: (K, V) => Unit, get: K => Option[V]): V =
    get(key).getOrElse {
      val v = load
      put(key, v)
      v
    }
}
