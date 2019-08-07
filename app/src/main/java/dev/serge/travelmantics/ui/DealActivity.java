package dev.serge.travelmantics.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

import dev.serge.travelmantics.R;
import dev.serge.travelmantics.model.TravelDeal;
import dev.serge.travelmantics.utils.FirebaseUtils;

public class DealActivity extends AppCompatActivity {

    private static final int PICTURE_RESULT = 42;
    EditText dealTitle;
    EditText dealDescription;
    EditText dealPrice;
    ImageButton dealImage;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private StorageReference storageReference;
    private String downloadUrl;
    private Uri imageUri;
    TravelDeal deal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("deals");
        storageReference = FirebaseUtils.storageReference;

        dealTitle = findViewById(R.id.inputTitle);
        dealDescription = findViewById(R.id.inputDescription);
        dealPrice = findViewById(R.id.inputPrice);
        dealImage = findViewById(R.id.dealImage);

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
        Picasso.get()
                .load(deal.getImageUrl())
                .into(dealImage);

        dealImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(DealActivity.this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_delete, menu);
        if (FirebaseUtils.isAdmin) {
            menu.findItem(R.id.save).setVisible(true);
            menu.findItem(R.id.delete).setVisible(true);
            enableEditext(true);
        } else {
            menu.findItem(R.id.save).setVisible(false);
            menu.findItem(R.id.delete).setVisible(false);
            enableEditext(false);
            dealImage.isClickable();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == RESULT_OK) {
            imageUri = result.getUri();
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Exception exception = result.getError();
            Log.d("ImageError", exception.getMessage());
        }

        dealImage.setImageURI(imageUri);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveDeal();
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
            return;
        }
        //databaseReference.child(deal.getId()).removeValue();
        if (deal.getImageName() != null && !deal.getImageName().isEmpty()) {
            StorageReference picRef = FirebaseUtils.storage.getReference().child(deal.getImageName());
            picRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("Image", "deleted");
                }
            });
        }
    }

    private void saveDeal() {
        if (deal.getId() != null) {
            deal.setTitle(dealTitle.getText().toString());
            deal.setDescription(dealDescription.getText().toString());
            deal.setTitle(dealPrice.getText().toString());
            deal.setPrice(dealPrice.getText().toString());
            deal.setImageUrl(deal.getImageUrl());
        } else {
            final StorageReference reference = storageReference.child("deals_pic")
                    .child(Objects.requireNonNull(imageUri.getLastPathSegment()));
            reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    String picName = task.getResult().getStorage().getPath();
                    deal.setImageName(picName);
                    if (task.isSuccessful()) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadUrl = uri.toString();
                                deal.setImageUrl(downloadUrl);
                                deal.setTitle(dealTitle.getText().toString());
                                deal.setDescription(dealDescription.getText().toString());
                                deal.setPrice(dealPrice.getText().toString());

                                databaseReference.push().setValue(deal);
                            }
                        });
                    }
                }
            });
        }
    }

    private void backToList() {
        Intent intent = new Intent(this, DealsListActivity.class);
        startActivity(intent);
    }

//    private void clean() {
//        dealTitle.setText("");
//        dealDescription.setText("");
//        dealPrice.setText("");
//        dealTitle.requestFocus();
//        finish();
//    }

    public void enableEditext(boolean isEnabled) {
        dealTitle.setEnabled(isEnabled);
        dealDescription.setEnabled(isEnabled);
        dealPrice.setEnabled(isEnabled);
    }
}
