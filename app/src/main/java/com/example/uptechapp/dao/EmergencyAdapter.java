package com.example.uptechapp.dao;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uptechapp.R;
import com.example.uptechapp.model.Emergency;

import java.util.List;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.ViewHolder> {

    private List<Emergency> emergenciesList;
    static final String TAG = "AdapterEmergency";
    private Context context;

    public EmergencyAdapter(List<Emergency> emergenciesList, Context context) {
        Log.d(TAG, "EmergencyAdapter: " + emergenciesList);
        this.emergenciesList = emergenciesList;
        this.context = context;
        Log.d(TAG, "EmergencyAdapter: CREATE");
    }

    @NonNull
    @Override
    public EmergencyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emergency2, parent, false);
        Log.d(TAG, "onCreateViewHolder: return");
        return new EmergencyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: start");
        Emergency emergency = emergenciesList.get(position);

        Log.d(TAG, "onBindViewHolder: go");
        Log.i(TAG, "Emergency - " + emergency.getTitle());

        holder.setData(
                emergency.getTitle(),
                emergency.getDescription(),
                emergency.getTime(),
                emergency.getPhotoUrl()
        );
    }

    @Override
    public int getItemCount() {
        return emergenciesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView emergencyTitle, emergencyDescription, emergencyTime;
        private ImageView emergencyPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            emergencyTitle = itemView.findViewById(R.id.textLabel);
            emergencyDescription = itemView.findViewById(R.id.textDescription);
            emergencyTime = itemView.findViewById(R.id.textDate);
            emergencyPhoto = itemView.findViewById(R.id.imageView);
            emergencyPhoto.setClipToOutline(true);

        }

        private void setData(String title, String description, String time, String photo) {

        try {
            emergencyTitle.setText(title);
            emergencyDescription.setText(description);
            emergencyTime.setText(time.toString());
//            emergencyPhoto.setImageResource(R.drawable.ic_google_logo);
            Glide.with(context).load(photo).into(emergencyPhoto);
         } catch (Exception e) {
                Log.i(TAG, e.getMessage());
            }
        }
    }
}
