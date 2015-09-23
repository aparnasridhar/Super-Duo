package barqsoft.footballscores;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import barqsoft.footballscores.service.RemoteViewWidgetService;

/**
 * Created by aparnasridhar on 9/23/15.
 */
public class ScoreWidgetProvider extends AppWidgetProvider {

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onUpdate(Context context, AppWidgetManager
            appWidgetManager,int[] appWidgetIds) {

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int appWidgetId : appWidgetIds) {

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.score_widget);

            //Populate the list
            Intent intent = new Intent(context, RemoteViewWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            remoteViews.setRemoteAdapter(R.id.widgetList, intent);

            //Configure a pending intent to MainActivity to handle clicks on the widget
            Intent configIntent = new Intent(context, MainActivity.class);
            PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.main_widget_container, configPendingIntent);

            //Intent for click on listview
            PendingIntent startActivityPendingIntent = PendingIntent.getActivity(context, 0, configIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.widgetList, startActivityPendingIntent);

            //Tell AppWidget Manager to perform update on current widget
            appWidgetManager.updateAppWidget(appWidgetId,remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
