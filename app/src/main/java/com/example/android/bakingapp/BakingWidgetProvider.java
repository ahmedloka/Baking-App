package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.bakingapp.database.AppDatabase;
import com.example.android.bakingapp.database.AppExecutor;
import com.example.android.bakingapp.widget.WidgetRemoteService;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);
        Intent serviceIntent = new Intent(context, WidgetRemoteService.class);
        views.setRemoteAdapter(R.id.list_view_widget, serviceIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (final int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(
                    context.getPackageName()
                    , R.layout.baking_widget_provider);

            setWidgetTitle(context, appWidgetManager, appWidgetId);

            Intent serviceIntent = new Intent(context, WidgetRemoteService.class);
            views.setRemoteAdapter(R.id.list_view_widget, serviceIntent);
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private void setWidgetTitle(final Context context, final AppWidgetManager appWidgetManager, final int appWidgetId) {
        AppExecutor.getInstance(context).diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<String> nameList = AppDatabase.getInstance(context).taskDao().getName();
                if (nameList != null) {
                    if (nameList.size() - 1 == -1) {
                        return;
                    }
                    int index = nameList.size() - 1;
                    String name = nameList.get(index).replace('[', ' ')
                            .replace(']', ' ')
                            .replace('"', ' ');

                    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);
                    views.setTextViewText(R.id.text_view_name_widget, name.trim() + "'s recipe");
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                }
            }
        });
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


}

