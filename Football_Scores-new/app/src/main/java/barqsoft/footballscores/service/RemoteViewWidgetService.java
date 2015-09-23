package barqsoft.footballscores.service;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;

/**
 * Created by aparnasridhar on 9/23/15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RemoteViewWidgetService extends RemoteViewsService {
    @Override public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return (new ScoreFactory(this.getApplicationContext(), intent));
    }

    public class ScoreFactory implements RemoteViewsService.RemoteViewsFactory {
        private Cursor scoreCursor;
        private Context context = null;

        public ScoreFactory(Context context, Intent intent) {
            this.context = context;
            getCursorData();
        }


        private void getCursorData() {
            scoreCursor = context.getContentResolver().query(DatabaseContract.BASE_CONTENT_URI, null, null, null, null);
            Log.d("count", scoreCursor.getCount() + "");
        }

        @Override public void onCreate() {
        }

        @Override public void onDataSetChanged() {
            getCursorData();
        }

        @Override public void onDestroy() {
        }

        @Override
        public int getCount() {
            return scoreCursor.getCount();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override public boolean hasStableIds() {
            return false;
        }

        @Override public RemoteViews getLoadingView() {
            return null;
        }

        @Override public int getViewTypeCount() {
            return 1;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN) @Override
        public RemoteViews getViewAt(int position) {


            final RemoteViews remoteView = new RemoteViews(
                    context.getPackageName(), R.layout.widget_item_layout);

            int padding = (int) context.getResources().getDimension(R.dimen.widget_padding);
            remoteView.setViewPadding(R.id.widget_container,padding,padding,padding,padding);

            scoreCursor.moveToPosition(position);
            String date = scoreCursor.getString(scoreCursor.getColumnIndex("date"));
            String home = scoreCursor.getString(scoreCursor.getColumnIndex("home"));
            String away = scoreCursor.getString(scoreCursor.getColumnIndex("away"));
            String homeGoals = scoreCursor.getString(scoreCursor.getColumnIndex("home_goals"));
            String awayGoals = scoreCursor.getString(scoreCursor.getColumnIndex("away_goals"));


            if(Integer.valueOf(homeGoals)==-1){
                homeGoals = "";
            }
            if (Integer.valueOf(awayGoals)==-1){
                awayGoals = "";
            }
            remoteView.setTextViewText(R.id.data_textview, date);
            remoteView.setTextViewText(R.id.home_name,home);
            remoteView.setTextViewText(R.id.away_name, away);
            remoteView.setTextViewText(R.id.score_textview, homeGoals + " : " + awayGoals);

            Intent fillInIntent = new Intent();
            remoteView.setOnClickFillInIntent(R.id.widget_container, fillInIntent);

            return remoteView;
        }

    }
}
