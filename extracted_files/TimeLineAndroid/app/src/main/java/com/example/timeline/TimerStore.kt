package com.example.timeline

import android.content.Context

object TimerStore {
    private const val PREFS = "timeline_settings"
    private const val START = "start_ms"
    private const val END = "end_ms"
    private const val DOWN = "count_down"
    private const val VERTICAL = "vertical"
    private const val LABEL = "label"

    fun prefs(context: Context) = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    fun start(context: Context) = prefs(context).getLong(START, 0L)
    fun end(context: Context) = prefs(context).getLong(END, 0L)
    fun countDown(context: Context) = prefs(context).getBoolean(DOWN, true)
    fun vertical(context: Context) = prefs(context).getBoolean(VERTICAL, false)
    fun label(context: Context) = prefs(context).getString(LABEL, "MY TIMELINE") ?: "MY TIMELINE"

    fun save(context: Context, durationMs: Long, countDown: Boolean, vertical: Boolean, label: String) {
        val now = System.currentTimeMillis()
        prefs(context).edit()
            .putLong(START, now)
            .putLong(END, now + durationMs)
            .putBoolean(DOWN, countDown)
            .putBoolean(VERTICAL, vertical)
            .putString(LABEL, label.ifBlank { "MY TIMELINE" })
            .apply()
    }

    fun remaining(context: Context): Long = (end(context) - System.currentTimeMillis()).coerceAtLeast(0L)
    fun elapsed(context: Context): Long = (System.currentTimeMillis() - start(context)).coerceAtLeast(0L)
}
