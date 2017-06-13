package android.rss;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;


public class ServiceRss extends Service {
    private NewsRSS mNewsRss;
    private Timer mTimer;
    private ArrayList<RSSItem> rssItems = new ArrayList<RSSItem>();
    private int mSizeArray;
    private Thread mThread;
    private int mId = 12;
    DataBaseSupport helper;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        helper = new DataBaseSupport(this);
        DataBaseManager manager = new DataBaseManager(helper);
        rssItems = manager.GetNewsArrayFromDataBase();
        Collections.reverse(rssItems);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSizeArray = intent.getIntExtra("size", 0);
        mNewsRss = new NewsRSS();
        mThread = new Thread(mNewsRss, "service");
        mNewsRss.setContext(getApplicationContext());
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!mThread.isAlive()) {
                    updateNews();
                }
            }
        },1000,6000);

        return START_STICKY;
    }

    public void  updateNews()
    {
        mThread.start();
        if (mNewsRss.isUpdate() && mNewsRss.getNumberItems() > mSizeArray) {
            SendNotifications();
            mSizeArray = mNewsRss.getNumberItems();
            mNewsRss.setIsUpdate(false);
        }
        mThread.join();

    }

    private void SendNotifications() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("News")
                        .setContentText("You have " + String.valueOf(mNewsRss.getNumberItems() - mSizeArray) + " news");
        Intent resultIntent = new Intent(this, NewsRSS.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(RSSItemDisplayer.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(mId, mBuilder.build());
    }
}
