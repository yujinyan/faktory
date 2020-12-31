package me.yujinyan.facktory

import java.time.LocalDateTime
import java.time.Period

fun now(): LocalDateTime = LocalDateTime.now()

fun today() = now().toLocalDate()

val Int.months: Period get() = Period.ofMonths(this)
