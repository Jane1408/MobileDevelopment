package com.example.notes;

import android.support.annotation.NonNull;

public class NoteSample {

    private String mElementTitle = "";
    private String mDescription = "";
    private String mDate = "";
    private String mPriority = "";

    private boolean mIsDone;
    private int mDbId;

    public NoteSample(String title, String description, String date, String priority, boolean isDone, int id) {
        mElementTitle = title;
        mDescription = description;
        mDate = date;
        mIsDone = isDone;
        mPriority = priority;
        mDbId = id;
    }

    public int GetId() {
        return mDbId;
    }

    public void SetChecked(boolean isDone) {
        mIsDone = isDone;
    }

    public String GetTitle() {
        return mElementTitle;
    }

    public String GetDescription() {
        return mDescription;
    }

    public String GetDate() {
        return mDate;
    }

    public boolean IsDone() {
        return mIsDone;
    }

    public String GetPriority() {
        return mPriority;
    }
}
