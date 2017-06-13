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

public class AddActivity extends Activity implements RadioGroup.OnCheckedChangeListener {

    public String mPriority = "Low";
    public String mDate = "";
    private Button mButton;
    private RadioGroup mGroup;
    private DataBaseManager helper;
    private CalendarView mCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.note_add_activity);
        mButton = (Button) findViewById(R.id.acceptBtn);
        mButton.setOnClickListener(onClickListener);
        mGroup = (RadioGroup) findViewById(R.id.priority_group);
        mGroup.setOnCheckedChangeListener(this);
        helper = new DataBaseManager(this);

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

                SQLiteDatabase db = helper.getWritableDatabase();
                db.insert("ToDoList", null, value);
            }
            helper.close();
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

}
