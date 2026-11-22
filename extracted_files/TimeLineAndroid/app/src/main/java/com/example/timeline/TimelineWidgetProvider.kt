package com.example.timeline

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.widget.RemoteViews
import java.util.concurrent.TimeUnit

class TimelineWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        ids.forEach { updateOne(context, manager, it) }
        schedule(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_REFRESH) refreshAll(context)
    }

    companion object {
        const val ACTION_REFRESH = "com.example.timeline.REFRESH"

        fun schedule(context: Context) {
            val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimelineWidgetProvider::class.java).setAction(ACTION_REFRESH)
            val pending = PendingIntent.getBroadcast(context, 44, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            alarm.cancel(pending)
            alarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 60_000L, 60_000L, pending)
        }

        fun refreshAll(context: Context) {
            val manager = AppWidgetManager.getInstance(context)
            val component = ComponentName(context, TimelineWidgetProvider::class.java)
            manager.getAppWidgetIds(component).forEach { updateOne(context, manager, it) }
        }

        private fun updateOne(context: Context, manager: AppWidgetManager, id: Int) {
            val vertical = TimerStore.vertical(context)
            val views = RemoteViews(context.packageName, if (vertical) R.layout.widget_vertical else R.layout.widget_horizontal)
            val start = TimerStore.start(context)
            val end = TimerStore.end(context)
            val valid = start > 0L && end > start
            val now = System.currentTimeMillis()
            val total = if (valid) end - start else 1L
            val elapsed = if (valid) (now - start).coerceIn(0L, total) else 0L
            val left = if (valid) (end - now).coerceAtLeast(0L) else 0L
            val progress = if (valid) ((elapsed.toDouble() / total.toDouble()) * 1000).toInt().coerceIn(0, 1000) else 0
            val showMs = if (TimerStore.countDown(context)) left else elapsed
            val timeText = if (valid) format(showMs) else "--:--:--"
            views.setTextViewText(R.id.widget_title, TimerStore.label(context).uppercase())
            views.setTextViewText(R.id.widget_time, timeText)
            views.setProgressBar(R.id.widget_progress, 1000, progress, false)
            val open = Intent(context, MainActivity::class.java)
            val click = PendingIntent.getActivity(context, 0, open, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.widget_root, click)
            manager.updateAppWidget(id, views)
        }

        private fun format(ms: Long): String {
            val seconds = TimeUnit.MILLISECONDS.toSeconds(ms)
            val h = seconds / 3600
            val m = (seconds % 3600) / 60
            val s = seconds % 60
            return String.format("%02d:%02d:%02d", h, m, s)
        }
    }
}
