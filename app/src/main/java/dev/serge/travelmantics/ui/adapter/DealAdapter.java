package dev.serge.travelmantics.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import dev.serge.travelmantics.R;
import dev.serge.travelmantics.model.TravelDeal;
import dev.serge.travelmantics.ui.DealActivity;
import dev.serge.travelmantics.utils.FirebaseUtils;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder> {

    private ArrayList<TravelDeal> deals;
    private FirebaseDatabase database;
    private ChildEventListener eventListener;
    private DatabaseReference mReference;

    public DealAdapter() {
        database = FirebaseDatabase.getInstance();
        mReference = database.getReference().child("deals");
        deals = new ArrayList<>();
        eventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelDeal deal = dataSnapshot.getValue(TravelDeal.class);
                deal.setId(dataSnapshot.getKey());
                deals.add(deal);
                notifyItemInserted(deals.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mReference.addChildEventListener(eventListener);
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_deal_layout, parent, false);

        return new DealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        TravelDeal deal = deals.get(position);
        holder.bind(deal);
    }

    @Override
    public int getItemCount() {
        deals = FirebaseUtils.deals;
        return deals.size();
    }

    class DealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dealTitle;
        TextView dealDescription;
        TextView dealPrice;

        DealViewHolder(@NonNull View itemView) {
            super(itemView);
            dealTitle = itemView.findViewById(R.id.tvDealTitle);
            dealDescription = itemView.findViewById(R.id.tvDealDesc);
            dealPrice = itemView.findViewById(R.id.tvDealPrice);
            itemView.setOnClickListener(this);
        }

        void bind(TravelDeal deal) {
            dealTitle.setText(deal.getTitle());
            dealDescription.setText(deal.getDescription());
            dealPrice.setText(deal.getPrice());
        }

        @Override
        public void onClick(View view) {
            if (FirebaseUtils.isAdmin) {
                int position = getAdapterPosition();
                TravelDeal selectedDeal = deals.get(position);
                Intent intent = new Intent(view.getContext(), DealActivity.class);
                intent.putExtra("deal", selectedDeal);
                view.getContext().startActivity(intent);
            } else {
                // todo how a dialog with action for non admin users.
            }
        }
    }
}
