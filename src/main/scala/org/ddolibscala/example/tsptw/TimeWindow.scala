package org.ddolibscala.example.tsptw

/** Represents a time window with a start and end time.
  *
  * <p> A `TimeWindow` defines an interval [start, end] during which an event, task, or visit must
  * occur. Both `start` and `end` are inclusive and usually represented as integer time units (e.g.,
  * minutes, hours). </p>
  *
  * <p> Typical usage is in scheduling problems such as the Traveling Salesman Problem with Time
  * Windows (TSPTW), where each location or task has a limited time window in which it can be
  * visited or executed. </p>
  *
  * @param start
  *   the beginning of the time window (inclusive)
  * @param end
  *   the end of the time window (inclusive)
  */
case class TimeWindow(start: Int, end: Int)
