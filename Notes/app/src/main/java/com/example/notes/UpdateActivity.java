package com.example.notes;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioGroup;

public class UpdateActivity extends Activity implements RadioGroup.OnCheckedChangeListener {

    public String mPriority = "Low";
    public String mDate = "";
    private Button mButton;
    private RadioGroup mGroup;
    private DataBaseManager manager;
    private CalendarView mCalendarView;

    private long mRecordId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecordId = getIntent().getLongExtra(getString(R.string.extra_record_id), 0);

        setContentView(R.layout.update_activity);
        mButton = (Button) findViewById(R.id.updateBtn);
        mButton.setOnClickListener(onClickListener);
        mGroup = (RadioGroup) findViewById(R.id.priority_group);
        mGroup.setOnCheckedChangeListener(this);
        manager = new DataBaseManager(this);

        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                int y = year;
                int m = month;
                int d = dayOfMonth;
                mDate = new StringBuilder()
                        .append(m + 1)
                        .append("-").append(d)
                        .append("-").append(y)
                        .append(" ").toString();
            }
        });
        UpdateActivityInit();
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            EditText titleEditText = (EditText) findViewById(R.id.editTitle);
            EditText descrEditText = (EditText) findViewById(R.id.editDescr);


            if (!titleEditText.getText().toString().isEmpty()) {

                ContentValues value = new ContentValues();
                value.put("title", titleEditText.getText().toString());
                value.put("description", descrEditText.getText().toString());
                value.put("priority", mPriority);
                value.put("isDone", 0);
                value.put("date", mDate);

                SQLiteDatabase db = manager.getWritableDatabase();
                db.update("ToDoList", value, "id = ?", new String[]{String.valueOf(mRecordId)});
            }
            manager.close();
            finish();
        }
    };

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.lowPriority:
                mPriority = getString(R.string.low);
                break;
            case R.id.middlePriority:
                mPriority = getString(R.string.middle);
                break;
            case R.id.highPriority:
                mPriority = getString(R.string.high);
                break;
            default:
                break;
        }
    }

    private void UpdateActivityInit(){
        SQLiteDatabase db = manager.getWritableDatabase();

        EditText titleEditText = (EditText) findViewById(R.id.editTitle);
        EditText descrEditText = (EditText) findViewById(R.id.editDescr);

        Cursor cursor = db.query("ToDoList", null, "id = ?", new String[]{String.valueOf(mRecordId)}, null, null, null);

        if(cursor.moveToFirst())
        {
            titleEditText.setText(cursor.getString(cursor.getColumnIndex("title")));
            descrEditText.setText(cursor.getString(cursor.getColumnIndex("description")));
        }
        mButton.setText("Update");
    }
}
