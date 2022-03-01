package com.windhans.client.forworld.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.windhans.client.forworld.Adapter.DetailsAdapter;
import com.windhans.client.forworld.R;

import java.util.ArrayList;

public class ActivityDetails extends AppCompatActivity {

    RecyclerView recyclerView;
    DetailsAdapter groupAdapter;
    ArrayList<String> stringArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        recyclerView = findViewById(R.id.recycler_view);
        stringArrayList = new ArrayList<>();
        stringArrayList.add(0, "India");
        stringArrayList.add(1, "China");
        stringArrayList.add(2, "NewZeland");
        stringArrayList.add(3, "Pakistan");
        stringArrayList.add(4, "West Indies");
        stringArrayList.add(5, "South Africa");

        groupAdapter = new DetailsAdapter(this, stringArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(groupAdapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(groupAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);


    }
}
