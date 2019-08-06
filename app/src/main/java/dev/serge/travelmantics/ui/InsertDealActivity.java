package dev.serge.travelmantics.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import dev.serge.travelmantics.R;
import dev.serge.travelmantics.model.TravelDeal;
import dev.serge.travelmantics.utils.FirebaseUtils;

public class InsertDealActivity extends AppCompatActivity {

    private DatabaseReference reference;
    EditText dealTitle;
    EditText dealDescription;
    EditText dealPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_deal);
        reference = FirebaseUtils.databaseReference;
        dealTitle = findViewById(R.id.inputTitle);
        dealPrice = findViewById(R.id.inputPrice);
        dealDescription = findViewById(R.id.inputDescription);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.insert_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveDeal:
                saveDeal();
                Toast.makeText(this, "Deal added successufuly", Toast.LENGTH_SHORT).show();
                clean();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    private void clean() {
        dealTitle.setText("");
        dealDescription.setText("");
        dealPrice.setText("");
        dealTitle.requestFocus();
        finish();
    }

    private void saveDeal() {
        String title = dealTitle.getText().toString().trim();
        String price = dealPrice.getText().toString().trim();
        String description = dealDescription.getText().toString().trim();
        // create a new deal object and insert it into our database
        TravelDeal deal = new TravelDeal(title, price, description,"");
        reference.push().setValue(deal);
    }
}
