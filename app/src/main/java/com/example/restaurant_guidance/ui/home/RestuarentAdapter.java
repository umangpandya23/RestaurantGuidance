package com.example.restaurant_guidance.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurant_guidance.R;
import com.example.restaurant_guidance.ui.InformationActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RestuarentAdapter extends RecyclerView.Adapter<RestuarentAdapter.ListItemHolder>{

    private Home home;
    private ArrayList<Restuarent> restuarentList;
    private double CurrentLat = 39.746661;
    private double CurrentLong= -105.002324;

    public RestuarentAdapter(Home home, ArrayList<Restuarent> restuarentList){
        this.home = home;
        this.restuarentList = restuarentList;
    }

    @NonNull
    @Override
    public ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ListItemHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemHolder holder, int position) {
        Restuarent restuarent = restuarentList.get(position);
        holder.textViewName.setText(restuarent.getName());
        holder.textViewAddress.setText(restuarent.getAddress());
        holder.textViewCity.setText(restuarent.getCity() + ", " + restuarent.getState() + ", " + restuarent.getZipcode());
        holder.textViewDistance.setText( restuarent.getDistance() + " mi");
    }

    @Override
    public int getItemCount() {
        return restuarentList.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewName;
        private TextView textViewAddress;
        private TextView textViewCity;
        private TextView textViewDistance;

        public ListItemHolder (View view) {
            super(view);

            textViewName = view.findViewById(R.id.textViewName);
            textViewAddress = view.findViewById(R.id.textViewAddress);
            textViewCity = view.findViewById(R.id.textViewCity);
            textViewDistance = view.findViewById(R.id.textViewDistance);
            view.setClickable(true);
            view.setOnClickListener(this);
        }

        public void onClick (View view) {
            home.inform(getAdapterPosition());
        }

    }

}

