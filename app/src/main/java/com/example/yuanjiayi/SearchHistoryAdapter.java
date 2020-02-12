package com.example.yuanjiayi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.historyViewHolder> {

    private List<String> searchHistory;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public SearchHistoryAdapter(List<String> searchHistory, Context context){
        this.searchHistory = searchHistory;
        this.context = context;
    }

    @NonNull
    @Override
    public historyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_searchhistory, parent, false);
        return new historyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull historyViewHolder holders, int position) {
        final historyViewHolder holder = holders;
        String oneHistory = searchHistory.get(position);

        holder.text_searchhistory.setText(oneHistory);
    }

    @Override
    public int getItemCount() {
        return searchHistory.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public class historyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView text_searchhistory;
        OnItemClickListener onItemClickListener;

        public historyViewHolder(View itemView, OnItemClickListener onItemClickListener) {

            super(itemView);

            itemView.setOnClickListener(this);
            text_searchhistory = itemView.findViewById(R.id.search_history_text);

            this.onItemClickListener = onItemClickListener;

        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
