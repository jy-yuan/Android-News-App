package com.example.yuanjiayi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BlocklistAdapter extends RecyclerView.Adapter<BlocklistAdapter.BlocklistViewHolder> {

    private List<String> blocklist;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public BlocklistAdapter(List<String> Blocklist, Context context){
        this.blocklist = Blocklist;
        this.context = context;
    }

    @NonNull
    @Override
    public BlocklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_searchhistory, parent, false);
        return new BlocklistViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BlocklistViewHolder holders, int position) {
        final BlocklistViewHolder holder = holders;
        String blockstr = blocklist.get(position);

        holder.text_blockstr.setText(blockstr);
    }

    @Override
    public int getItemCount() {
        return blocklist.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public class BlocklistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView text_blockstr;
        OnItemClickListener onItemClickListener;

        public BlocklistViewHolder(View itemView, OnItemClickListener onItemClickListener) {

            super(itemView);

            itemView.setOnClickListener(this);
            text_blockstr = itemView.findViewById(R.id.search_history_text);

            this.onItemClickListener = onItemClickListener;

        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
