package android.rss;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RSSViewAdapter extends RecyclerView
        .Adapter<RSSViewAdapter
        .DataObjectHolder> {
    private ArrayList<RSSItem> news;
    private static MyClickListener listener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView title;
        TextView dateTime;

        public DataObjectHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            dateTime = (TextView) itemView.findViewById(R.id.date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.listener = myClickListener;
    }

    public RSSViewAdapter(ArrayList<RSSItem> myDataset) {
        news = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.title.setText(news.get(position).getTitle());
        holder.dateTime.setText(news.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
