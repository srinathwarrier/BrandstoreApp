package com.brandstore1.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.brandstore1.R;
import com.brandstore1.activities.OutletListActivity;
import com.brandstore1.adapters.AveragePriceAdapter;
import com.brandstore1.adapters.FloorAdapter;
import com.brandstore1.adapters.GenderAdapter;
import com.brandstore1.adapters.MoreFiltersAdapter;
import com.brandstore1.adapters.SortAdapter;
import com.brandstore1.entities.Outlet;
import com.brandstore1.entities.OutletListFilterConstraint;
import com.brandstore1.views.DividerItemDecoration;

import java.util.ArrayList;

/**
 * Created by Ravi on 03-Oct-15.
 */
public class OutletListFilters extends Fragment {
    private static final String TAG = "OLFiltersFragment";
    public static boolean isOpen = false;
    RecyclerView rvAveragePrice, rvGender, rvMoreFilters,rvSort ,rvFloor;
    AveragePriceAdapter averagePriceAdapter;
    GenderAdapter genderAdapter;
    MoreFiltersAdapter moreFiltersAdapter;
    SortAdapter sortAdapter;
    FloorAdapter floorAdapter;
    LinearLayoutManager genderLayoutManager, moreFiltersLayoutManger, averagePriceLayoutManager , sortLayoutManager , floorLayoutManager;

    ArrayList<String> genderArray ;
    ArrayList<String> moreFiltersArray ;
    ArrayList<String> averagePriceArray ;
    ArrayList<String> sortArray ;
    ArrayList<String> floorArray ;

    Button applyFilterButton;

    OutletListFilterConstraint outletListFilterConstraint;
    Toolbar mToolbar;

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
        if(((OutletListActivity)getActivity()).getOutletListFilterConstraint()==null){
            outletListFilterConstraint = new OutletListFilterConstraint();
        }
        else{
            outletListFilterConstraint =((OutletListActivity)getActivity()).getOutletListFilterConstraint();
        }
        genderArray = outletListFilterConstraint.genderArrayList;
        moreFiltersArray = outletListFilterConstraint.moreFiltersArrayList;
        averagePriceArray = outletListFilterConstraint.averagePriceArrayList;
        sortArray = outletListFilterConstraint.sortArrayList;
        floorArray = outletListFilterConstraint.floorArrayList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        isOpen = true;
        View layout = inflater.inflate(R.layout.fragment_filters, container, false);
        getViewReferences(layout);
        initViews();

        //for create home button
        mToolbar.setNavigationIcon(R.drawable.ic_cancel_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().
                        remove(fragmentManager.findFragmentById(R.id.fl_filters)).commit();
            }
        });
        mToolbar.setTitle("");
        mToolbar.inflateMenu(R.menu.menu_fragment_filters);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.reset_button:
                        outletListFilterConstraint.setToResetState();
                        sortAdapter.notifyDataSetChanged();
                        averagePriceAdapter.notifyDataSetChanged();
                        genderAdapter.notifyDataSetChanged();
                        moreFiltersAdapter.notifyDataSetChanged();
                        floorAdapter.notifyDataSetChanged();
                        return true;
                }
                return false;
            }
        });


        applyFilterButton = (Button) layout.findViewById(R.id.btn_apply_filters);
        applyFilterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i(TAG, "Clicked");

                // Set outletListFilterConstraint
                ((OutletListActivity) getActivity()).setOutletListFilterConstraint(outletListFilterConstraint);

                // If outletListFilterConstraint has any change, then change Filter icon on Toolbar
                if (!outletListFilterConstraint.isInResetState()) {
                    ((OutletListActivity) getActivity()).setFilterIconSelected(true);
                }
                else {
                    ((OutletListActivity) getActivity()).setFilterIconSelected(false);
                }

                // Reset list
                ((OutletListActivity) getActivity()).resetArrayList();

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().
                        remove(fragmentManager.findFragmentById(R.id.fl_filters)).commit();
            }
        });
        return layout;
    }

    private void getViewReferences(View layout) {
        rvAveragePrice = (RecyclerView) layout.findViewById(R.id.rv_average_price);
        rvGender = (RecyclerView) layout.findViewById(R.id.rv_gender);
        rvMoreFilters = (RecyclerView) layout.findViewById(R.id.rv_more_filters);
        rvSort = (RecyclerView) layout.findViewById(R.id.rv_sort);
        rvFloor = (RecyclerView) layout.findViewById(R.id.rv_floor);
        mToolbar = (Toolbar) layout.findViewById(R.id.outletlistfiltertoolbar);
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
        genderAdapter = new GenderAdapter(genderArray, outletListFilterConstraint, getContext());
        averagePriceAdapter = new AveragePriceAdapter(averagePriceArray,outletListFilterConstraint , getActivity());
        moreFiltersAdapter = new MoreFiltersAdapter(moreFiltersArray,outletListFilterConstraint, getActivity());
        sortAdapter = new SortAdapter(sortArray, outletListFilterConstraint, getActivity());
        floorAdapter = new FloorAdapter(floorArray, outletListFilterConstraint, getActivity());

        genderLayoutManager = new LinearLayoutManager(getActivity());
        averagePriceLayoutManager = new LinearLayoutManager(getContext());
        moreFiltersLayoutManger = new LinearLayoutManager(getContext());
        sortLayoutManager = new LinearLayoutManager(getContext());
        floorLayoutManager = new LinearLayoutManager(getContext());

        rvAveragePrice.setLayoutManager(averagePriceLayoutManager);
        rvGender.setLayoutManager(genderLayoutManager);
        rvMoreFilters.setLayoutManager(moreFiltersLayoutManger);
        rvSort.setLayoutManager(sortLayoutManager);
        rvFloor.setLayoutManager(floorLayoutManager);

        rvAveragePrice.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        rvGender.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        rvMoreFilters.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        rvSort.addItemDecoration(new DividerItemDecoration(getActivity(),null));
        rvFloor.addItemDecoration(new DividerItemDecoration(getActivity(),null));

        rvAveragePrice.setAdapter(averagePriceAdapter);
        rvGender.setAdapter(genderAdapter);
        rvMoreFilters.setAdapter(moreFiltersAdapter);
        rvSort.setAdapter(sortAdapter);
        rvFloor.setAdapter(floorAdapter);
    }

    public void onApplyFilterClicked(View v){

    }
}
