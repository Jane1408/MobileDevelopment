package com.example.notes;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteFragment extends ListFragment {

    DataBaseManager manager;
    ArrayList<NoteSample> elements = new ArrayList<>();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        manager = new DataBaseManager(this.getActivity());
        GetNotes();
        ListAdapter adapter = new MyListAdapter(getActivity(),
                android.R.layout.simple_list_item_1, elements);
        setListAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        GetNotes();
        ListAdapter adapter = new MyListAdapter(getActivity(),
                android.R.layout.simple_list_item_1, elements);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.listfragment, null);
    }

    public void DoOnClick(View v, int position) {

        CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox);
        boolean isDone = elements.get(position).IsDone();
            elements.get(position).SetChecked(!isDone);

        checkBox.setChecked(elements.get(position).IsDone());

        SQLiteDatabase db = manager.getWritableDatabase();

        int doneStatus = 0;
        if (elements.get(position).IsDone()) {
            doneStatus = 1;
        }

        ContentValues value = new ContentValues();
        value.put("isDone", doneStatus);
        db.update("ToDoList", value, "id = ?", new String[]{String.valueOf(elements.get(position).GetId())});
    }

    private void GetNotes() {
        elements.clear();
        SQLiteDatabase db = manager.getWritableDatabase();
        Cursor cursor = db.query("ToDoList", null, null, null, null, null, "isDone");
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String priority = cursor.getString(cursor.getColumnIndex("priority"));
                boolean isDone = cursor.getInt(cursor.getColumnIndex("isDone")) > 0;
                NoteSample element = new NoteSample(title, description, date, priority, isDone, cursor.getInt(cursor.getColumnIndex("id")));
                elements.add(element);
            } while (cursor.moveToNext());
        }

    }

    private void RemoveNote(long id) {
        SQLiteDatabase db = manager.getWritableDatabase();
        db.delete("ToDoList", "id = " + id, null);
        GetNotes();
        ListAdapter adapter = new MyListAdapter(getActivity(),
                android.R.layout.simple_list_item_1, elements);
        setListAdapter(adapter);

    }

    public class MyListAdapter extends ArrayAdapter<NoteSample> {

        private Context mContext;

        public MyListAdapter(Context context, int textViewResourceId, ArrayList<NoteSample> elements) {
            super(context, textViewResourceId, elements);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.frame_row, parent,
                    false);

            TextView titleView = (TextView) row.findViewById(R.id.title);
            titleView.setText(elements.get(position).GetTitle());

            TextView descriptionView = (TextView) row.findViewById(R.id.description);
            descriptionView.setText(elements.get(position).GetDescription());

            TextView dateView = (TextView) row.findViewById(R.id.date);
            dateView.setText(elements.get(position).GetDate());

            TextView priorityView = (TextView) row.findViewById(R.id.priority);
            priorityView.setText(elements.get(position).GetPriority());

            CheckBox doneBox = (CheckBox) row.findViewById(R.id.checkBox);
            doneBox.setChecked(elements.get(position).IsDone());
            registerForContextMenu(row);

            row.setOnClickListener(new MyOnClickListener(row, position));

            CustomLongClickListener listener = new CustomLongClickListener(position);
            row.setOnLongClickListener(listener);
            return row;
        }

        public class MyOnClickListener implements View.OnClickListener {
            private int mPosition;
            private View mView;

            public MyOnClickListener(View view, int position) {
                mPosition = position;
                mView = view;
            }

            @Override
            public void onClick(View v) {
                DoOnClick(mView, mPosition);
            }
        }

        public class CustomLongClickListener implements View.OnLongClickListener {

            private int mPosition;

            public CustomLongClickListener(int position) {
                mPosition = position;
            }

            @Override
            public boolean onLongClick(View v) {
                ShowPopupMenu(v, elements.get(mPosition).GetId());
                return true;
            }
        }
    }

    private void ShowPopupMenu(View v, int recorfId){
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        popupMenu.inflate(R.menu.note_menu);

        popupMenu.setOnMenuItemClickListener(new MyPopupMenuListener(recorfId));
        popupMenu.show();
    }

    public class MyPopupMenuListener implements PopupMenu.OnMenuItemClickListener {

        private long mId;

        public MyPopupMenuListener(int pos){mId = pos;}

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {

                case R.id.Delete:
                    RemoveNote(mId);
                    return true;
                case R.id.Update:
                    Intent intent = new Intent(getActivity(), UpdateActivity.class);
                    intent.putExtra(getString(R.string.extra_record_id), mId);
                    intent.putExtra(getString(R.string.extra_is_update_flag), true);
                    startActivity(intent);
                    return true;
                default:
                    return false;
            }
        }
    }
}