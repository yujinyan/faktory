package me.yujinyan.facktory

import java.time.LocalDateTime
import java.time.Period

internal fun now(): LocalDateTime = LocalDateTime.now()

internal fun today() = now().toLocalDate()

internal val Int.months: Period get() = Period.ofMonths(this)
