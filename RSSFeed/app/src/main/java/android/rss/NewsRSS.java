package android.rss;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.*;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class NewsRSS extends AppCompatActivity implements Serializable{

    public static RSSItem selectedRssItem = null;
    private RecyclerView mRssListView = null;
    private RecyclerView.Adapter mRssAdapter = null;
    private RecyclerView.LayoutManager mLayoutManager;

    private String mRssFeedUrl = "https://lenta.ru/rss";

    private ArrayList<RSSItem> mRssItems = new ArrayList<RSSItem>();
    private NewsRSS self = this;

    private DataBaseManager manager;

    private Timer mTimer;
    private Context mContext = null;
    private Handler mHandler;

    private Thread mThread;
    private boolean mIsUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = new DataBaseManager(new DataBaseSupport(this));
        setContentView(R.layout.activity_rssfeed);
        InitNewsArray();

        Collections.reverse(mRssItems);

        mRssListView = (RecyclerView) findViewById(R.id.rssRecyclerView);
        mRssListView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRssListView.setLayoutManager(mLayoutManager);

        mRssAdapter = new RSSViewAdapter(mRssItems);
        mRssListView.setAdapter(mRssAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRssListView.addItemDecoration(itemDecoration);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                mRssAdapter = new RSSViewAdapter(mRssItems);
                mRssListView.setAdapter(mRssAdapter);
                mRssAdapter.notifyDataSetChanged();
            }
        };
        mThread = new Thread(this, "feed");

        StartService();
        SetListenerOnAdapter();
        SetUpdateTimer();

    }

    @Override
    protected void onResume() {
        super.onResume();
        SetListenerOnAdapter();
    }

    public int getNumberItems(){
        return mRssItems.size();
    }

    public void SetUpdateTimer()
    {
        mTimer = new Timer("timer");
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!self.mThread.isAlive()) {
                    self.updateNews();
                }
            }
        },1000,5000);
    }

    public void updateRssList(String name) {

        DataBaseSupport db = new DataBaseSupport(mContext);

        if (manager == null) {
            manager = new DataBaseManager(db);
        }
        if (mRssItems.isEmpty()) {
            mRssItems = manager.GetNewsArrayFromDataBase();
        }
        mIsUpdate = false;
        RSSItem.getRssItems(mRssFeedUrl, db);
        ArrayList<RSSItem> newItems = manager.GetNewsArrayFromDataBase();
        int size = mRssItems.size();
        if (size == 0 || !mRssItems.get(0).getTitle().equals(newItems.get(0).getTitle()))
        {
            mIsUpdate = true;
            if (name.equals("feed")){
                InitNewsArray();
            }
        }
    }

    public boolean isUpdate()
    {
        return mIsUpdate;
    }

    public void setIsUpdate(boolean isUpdate) {mIsUpdate = isUpdate;}

    public void setContext(Context context)
    {
        mContext = context;
    }

    public void  updateNews()
    {
        mThread.start();
        mHandler.sendEmptyMessage(0);
    }

    private void SetListenerOnAdapter() {
        ((RSSViewAdapter) mRssAdapter).setOnItemClickListener(new RSSViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                selectedRssItem = mRssItems.get(position);
                Intent intent = new Intent(self, RSSItemDisplayer.class);
                intent.putExtra("description", selectedRssItem.getDescription());
                intent.putExtra("title", selectedRssItem.getTitle());
                intent.putExtra("link", selectedRssItem.getLink());
                intent.putExtra("date", selectedRssItem.getPubDate());
                startActivity(intent);
            }
        });
    }

    private void StartService()
    {
        try {
            Intent service = new Intent(this, ServiceRss.class);
            service.putExtra("size", mRssItems.size());
            startService(service);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void InitNewsArray(){mRssItems = manager.GetNewsArrayFromDataBase();}

}
