package com.bloomify.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.bloomify.R
import com.bloomify.utils.PreferenceManager

class HabitWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val preferenceManager = PreferenceManager(context)
        val habits = preferenceManager.getHabits()

        val completed = habits.count { it.isCompleted }
        val total = habits.size
        val percentage = if (total > 0) (completed * 100) / total else 0

        val views = RemoteViews(context.packageName, R.layout.widget_habit)
        views.setTextViewText(R.id.tvWidgetPercentage, "$percentage%")
        views.setTextViewText(R.id.tvWidgetProgress, "$completed/$total completed")

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}