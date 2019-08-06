package dev.serge.travelmantics.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dev.serge.travelmantics.R;
import dev.serge.travelmantics.model.TravelDeal;
import dev.serge.travelmantics.utils.FirebaseUtils;

public class DealActivity extends AppCompatActivity {

    EditText dealTitle;
    EditText dealDescription;
    EditText dealPrice;
    private DatabaseReference reference;
    private FirebaseDatabase database;
    TravelDeal deal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("deals");

        dealTitle = findViewById(R.id.editTitle);
        dealDescription = findViewById(R.id.editDescription);
        dealPrice = findViewById(R.id.editPrice);

        // get the deal intent from the previous activity
        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("deal");

        if (deal == null) {
            deal = new TravelDeal();
        }
        this.deal = deal;

        // fill the fields with the intent data
        dealTitle.setText(deal.getTitle());
        dealDescription.setText(deal.getDescription());
        dealPrice.setText(deal.getPrice());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                saveModifications();
                clean();
                backToList();
            case R.id.delete:
                deleteDeal();
                backToList();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    private void deleteDeal() {
        if (deal == null) {
            Toast.makeText(this, "No reccord to delete", Toast.LENGTH_SHORT).show();
        } else {
            reference.child(deal.getId()).removeValue();
        }
    }

    private void saveModifications() {
        deal.setTitle(dealTitle.getText().toString());
        deal.setDescription(dealDescription.getText().toString());
        deal.setTitle(dealPrice.getText().toString());
        if (deal.getId() == null) {
            reference.push().setValue(deal);
        } else {
            reference.child(deal.getId()).setValue(deal);
        }
    }

    private void backToList() {
        Intent intent = new Intent(this, DealsListActivity.class);
        startActivity(intent);
    }

    private void clean() {
        dealTitle.setText("");
        dealDescription.setText("");
        dealPrice.setText("");
        dealTitle.requestFocus();
        finish();
    }
}
