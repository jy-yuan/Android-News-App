package com.example.yuanjiayi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private boolean mybool;

    public CategoryAdapter(Context context, boolean mybool){
        this.mybool = mybool;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(layoutInflater.inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        int ccnt = -1;
        for (int i = 0; i < 10; i++){
            if (((SettingsActivity) context).cateBool[i] == mybool) {
                ccnt++;
            }
            if (ccnt == position) {
                ((CategoryViewHolder) holder).text.setText(MainActivity.categoryStr[i]);
                ((CategoryViewHolder) holder).image.setImageResource(MainActivity.categoryInt[i]);
                break;
            }
        }
        ((CategoryViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ccnt = -1;
                for (int i = 0; i < 10; i++) {
                    if (((SettingsActivity) context).cateBool[i] == mybool) {
                        ccnt++;
                    }
                    if (ccnt == position) {
                        ((SettingsActivity) context).cateBool[i] = !((SettingsActivity) context).cateBool[i];
                        int ddnt = 0;
                        for (int j = 0; j < i; j++) {
                            if (((SettingsActivity) context).cateBool[j] != mybool) {
                                ddnt++;
                            }
                        }
                        if (mybool) {
                            ((SettingsActivity) context).adaptera.notifyItemRemoved(position);
                            for (int j = 0; j < ((SettingsActivity) context).adaptera.getItemCount(); j++) {
                                ((SettingsActivity) context).adaptera.notifyItemChanged(j);
                            }
                            ((SettingsActivity) context).adapterb.notifyItemInserted(ddnt);
                            for (int j = 0; j < ((SettingsActivity) context).adapterb.getItemCount(); j++) {
                                ((SettingsActivity) context).adapterb.notifyItemChanged(j);
                            }
                        } else {
                            ((SettingsActivity) context).adapterb.notifyItemRemoved(position);
                            for (int j = 0; j < ((SettingsActivity) context).adapterb.getItemCount(); j++) {
                                ((SettingsActivity) context).adapterb.notifyItemChanged(j);
                            }
                            ((SettingsActivity) context).adaptera.notifyItemInserted(ddnt);
                            for (int j = 0; j < ((SettingsActivity) context).adaptera.getItemCount(); j++) {
                                ((SettingsActivity) context).adaptera.notifyItemChanged(j);
                            }
                        }
                        break;
                    }
                }
                ((SettingsActivity) context).ret();
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        int ccnt = 0;
        for(int i = 0; i < 10; i++){
            if(((SettingsActivity) context).cateBool[i] == mybool) {
                ccnt++;
            }
        }
        return ccnt;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{

        TextView text;
        ImageView image;

        public CategoryViewHolder(View itemView) {

            super(itemView);
            text = itemView.findViewById(R.id.cate_text);
            image = itemView.findViewById(R.id.cate_image);
        }
    }

}
