package android.rss;


public class Thread implements Runnable {
    private java.lang.Thread thread;
    private NewsRSS rss;
    private boolean mIsAlive = false;
    private int mNumber = 0;
    private String mName = "";

    Thread(NewsRSS newsRss, String name) {
        rss = newsRss;
        mName = name;
        CreateThread();
    }

    public void run() {
        mIsAlive = true;
        rss.updateRssList(mName);
        mIsAlive = false;
    }

    public void join()
    {
        try {
            if (thread.isAlive()) {
                thread.join();
            }
        }catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }

    public boolean isAlive()
    {
        return mIsAlive;
    }

    public void start()
    {
        CreateThread();
        mIsAlive = true;
        thread.start();
    }

    private void CreateThread(){thread = new java.lang.Thread(this, mName + String.valueOf(mNumber++));}

}
