package com.brandstore1.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brandstore1.R;
import com.brandstore1.entities.OutletListFilterConstraint;

import java.util.ArrayList;

/**
 * Created by Ravi on 04-Oct-15.
 */
public class FloorAdapter extends RecyclerView.Adapter<FloorAdapter.CustomViewHolder> {
    private static final String TAG = "FloorAdapter";
    ArrayList<String> floorArray = new ArrayList<>();
    OutletListFilterConstraint outletListFilterConstraint;
    Context context;

    public FloorAdapter(ArrayList<String> floorArray, OutletListFilterConstraint outletListFilterConstraint, Context context) {
        this.floorArray= floorArray;
        this.outletListFilterConstraint = outletListFilterConstraint;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View v = layoutInflater.inflate(R.layout.floor_filter_item, parent, false);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        holder.tvFilterName.setText(floorArray.get(position));
        if(outletListFilterConstraint.getIsClicked(holder.tvFilterName.getText().toString())){
            setItemAsClicked(holder,true);
        }
        else{
            setItemAsClicked(holder,false);
        }
        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.isClicked) {
                    setItemAsClicked(holder,true);
                    outletListFilterConstraint.onFilterClicked(holder.tvFilterName.getText().toString(),true);
                } else {
                    setItemAsClicked(holder,false);
                    outletListFilterConstraint.onFilterClicked(holder.tvFilterName.getText().toString(),false);
                }
            }
        });
    }

    public void setItemAsClicked(final CustomViewHolder holder, boolean isSet){
        if(isSet){
            holder.isClicked = true;
            holder.ivIsFilterActive.setVisibility(View.VISIBLE);
            holder.tvFilterName.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
        else{
            holder.isClicked = false;
            holder.ivIsFilterActive.setVisibility(View.INVISIBLE);
            holder.tvFilterName.setTextColor(ContextCompat.getColor(context, R.color.material_grey_600));
        }
    }

    @Override
    public int getItemCount() {
        return floorArray.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView tvFilterName;
        ImageView ivIsFilterActive;
        LinearLayout llContainer;
        boolean isClicked = false;

        public CustomViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + v.toString() + " clicked.");
                }
            });
            llContainer = (LinearLayout) itemView.findViewById(R.id.ll_container);
            tvFilterName = (TextView) itemView.findViewById(R.id.tv_filter_name);
            ivIsFilterActive = (ImageView) itemView.findViewById(R.id.iv_filter);
        }
    }
}
