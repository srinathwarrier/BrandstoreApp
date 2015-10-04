package com.brandstore1.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandstore1.R;
import com.brandstore1.adapters.AveragePriceAdapter;
import com.brandstore1.adapters.GenderAdapter;
import com.brandstore1.adapters.MoreFiltersAdapter;
import com.brandstore1.views.DividerItemDecoration;

import java.util.ArrayList;

/**
 * Created by Ravi on 03-Oct-15.
 */
public class OutletListFilters extends Fragment {
    public static boolean isOpen = false;
    RecyclerView rvAveragePrice, rvGender, rvMoreFilters;
    AveragePriceAdapter averagePriceAdapter;
    GenderAdapter genderAdapter;
    MoreFiltersAdapter moreFiltersAdapter;
    LinearLayoutManager genderLayoutManager, moreFiltersLayoutManger, averagePriceLayoutManager;

    ArrayList<String> genderArray = new ArrayList<>();
    ArrayList<String> moreFiltersArray = new ArrayList<>();
    ArrayList<String> averagePriceArray = new ArrayList<>();

    public OutletListFilters() {

    }

    public static OutletListFilters getInstance() {
        OutletListFilters outletListFilters = new OutletListFilters();
        Bundle bundle = new Bundle();
        outletListFilters.setArguments(bundle);
        return outletListFilters;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO : Remove hardcoding
        genderArray.add("Male");
        genderArray.add("Female");
        genderArray.add("Children");
        moreFiltersArray.add("On Sale");
        moreFiltersArray.add("My Favourites");
        averagePriceArray.add("0-500");
        averagePriceArray.add("500-1500");
        averagePriceArray.add("1500-2500");
        averagePriceArray.add("2500-3500");
        averagePriceArray.add("3500+");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        isOpen = true;
        View layout = inflater.inflate(R.layout.fragment_filters, container, false);
        getViewReferences(layout);
        initViews();
        return layout;
    }

    private void getViewReferences(View layout) {
        rvAveragePrice = (RecyclerView) layout.findViewById(R.id.rv_average_price);
        rvGender = (RecyclerView) layout.findViewById(R.id.rv_gender);
        rvMoreFilters = (RecyclerView) layout.findViewById(R.id.rv_more_filters);

    }

    public static boolean isOpen() {
        return isOpen;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isOpen = false;
    }

    public void initViews() {
        genderAdapter = new GenderAdapter(genderArray, getContext());
        averagePriceAdapter = new AveragePriceAdapter(averagePriceArray, getActivity());
        moreFiltersAdapter = new MoreFiltersAdapter(moreFiltersArray, getActivity());
        genderLayoutManager = new LinearLayoutManager(getActivity());
        averagePriceLayoutManager = new LinearLayoutManager(getContext());
        moreFiltersLayoutManger = new LinearLayoutManager(getContext());
        rvAveragePrice.setLayoutManager(averagePriceLayoutManager);
        rvGender.setLayoutManager(genderLayoutManager);
        rvMoreFilters.setLayoutManager(moreFiltersLayoutManger);
        rvAveragePrice.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        rvGender.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        rvMoreFilters.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        rvAveragePrice.setAdapter(averagePriceAdapter);
        rvGender.setAdapter(genderAdapter);
        rvMoreFilters.setAdapter(moreFiltersAdapter);
    }
}
