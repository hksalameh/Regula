package com.example.util

import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

/** Calendar boundaries in the user's timezone, including DST transitions. */
object DateRanges {
    fun startOfToday(clock: Clock = Clock.systemDefaultZone()): Long =
        LocalDate.now(clock).atStartOfDay(clock.zone).toInstant().toEpochMilli()

    fun startOfNextDay(clock: Clock = Clock.systemDefaultZone()): Long =
        LocalDate.now(clock).plusDays(1).atStartOfDay(clock.zone).toInstant().toEpochMilli()

    fun startOfLookback(days: Int, clock: Clock = Clock.systemDefaultZone()): Long {
        require(days > 0)
        return LocalDate.now(clock).minusDays(days.toLong() - 1)
            .atStartOfDay(clock.zone).toInstant().toEpochMilli()
    }

    fun isToday(timestamp: Long, clock: Clock = Clock.systemDefaultZone()): Boolean =
        timestamp >= startOfToday(clock) && timestamp < startOfNextDay(clock)

    fun elapsedHours(timestamp: Long, clock: Clock = Clock.systemDefaultZone()): Long =
        ChronoUnit.HOURS.between(Instant.ofEpochMilli(timestamp), clock.instant()).coerceAtLeast(0)
}
