package com.brandstore1.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brandstore1.R;

import java.util.ArrayList;

/**
 * Created by Ravi on 04-Oct-15.
 */
public class GenderAdapter extends RecyclerView.Adapter<GenderAdapter.CustomViewHolder> {
    ArrayList<String> genderArray = new ArrayList<>();
    Context context;

    public GenderAdapter(ArrayList<String> genderArray, Context context) {
        this.genderArray = genderArray;
        this.context = context;
    }

    @Override
    public GenderAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View v = layoutInflater.inflate(R.layout.gender_filter_item, parent, false);
        return new GenderAdapter.CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        holder.tvFilterName.setText(genderArray.get(position));
        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.isClicked) {
                    holder.isClicked = true;
                    holder.ivIsFilterActive.setVisibility(View.VISIBLE);
                    holder.tvFilterName.setTextColor(ContextCompat.getColor(context, R.color.black));
                } else {
                    holder.isClicked = false;
                    holder.ivIsFilterActive.setVisibility(View.INVISIBLE);
                    holder.tvFilterName.setTextColor(ContextCompat.getColor(context, R.color.material_grey_600));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return genderArray.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView tvFilterName;
        ImageView ivIsFilterActive;
        LinearLayout llContainer;
        boolean isClicked = false;

        public CustomViewHolder(View itemView) {
            super(itemView);
            llContainer = (LinearLayout) itemView.findViewById(R.id.ll_container);
            tvFilterName = (TextView) itemView.findViewById(R.id.tv_filter_name);
            ivIsFilterActive = (ImageView) itemView.findViewById(R.id.iv_filter);
        }
    }
}
