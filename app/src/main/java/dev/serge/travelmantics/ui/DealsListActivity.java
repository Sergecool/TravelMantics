package dev.serge.travelmantics.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dev.serge.travelmantics.R;
import dev.serge.travelmantics.ui.adapter.DealAdapter;
import dev.serge.travelmantics.utils.FirebaseUtils;

public class DealsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        final DealAdapter adapter = new DealAdapter();
        RecyclerView recyclerView = findViewById(R.id.rvDeals);
        recyclerView.setAdapter(adapter);

        FirebaseUtils.openFirebaseRef("deals", this);
        FloatingActionButton fabAddDeal = findViewById(R.id.fabAddDeal);
        fabAddDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InsertDealActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtils.detachListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUtils.openFirebaseRef("deals", this);
        RecyclerView rv = findViewById(R.id.rvDeals);
        final DealAdapter adapter = new DealAdapter();
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        FirebaseUtils.attachListener();
    }
}