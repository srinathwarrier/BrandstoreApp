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
public class MoreFiltersAdapter extends RecyclerView.Adapter<MoreFiltersAdapter.CustomViewHolder> {
    ArrayList<String> moreFiltersArray = new ArrayList<>();
    Context context;

    public MoreFiltersAdapter(ArrayList<String> moreFiltersArray, Context context) {
        this.moreFiltersArray = moreFiltersArray;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View v = layoutInflater.inflate(R.layout.more_filter_item, parent, false);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        holder.tvFilterName.setText(moreFiltersArray.get(position));
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
        return moreFiltersArray.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView tvFilterName;
        ImageView ivIsFilterActive;
        LinearLayout llContainer;
        boolean isClicked;

        public CustomViewHolder(View itemView) {
            super(itemView);
            llContainer = (LinearLayout) itemView.findViewById(R.id.ll_container);
            tvFilterName = (TextView) itemView.findViewById(R.id.tv_filter_name);
            ivIsFilterActive = (ImageView) itemView.findViewById(R.id.iv_filter);
            isClicked = false;
        }
    }
}
