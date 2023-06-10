package com.example.uptechapp.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.uptechapp.R;
import com.example.uptechapp.api.CompleteListener;
import com.example.uptechapp.api.PickImage;
import com.example.uptechapp.dao.Database;
import com.example.uptechapp.dao.MapService;
import com.example.uptechapp.dao.MyViewModel;
import com.example.uptechapp.databinding.FragmentMapBinding;
import com.example.uptechapp.model.Emergency;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {

    private FragmentMapBinding binding;

    private List<Emergency> myEmergencyList;
    private static final int PICK_IMAGE_REQUEST = 1;

    MapService mapService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentMapBinding.inflate(getLayoutInflater());

        PickImage pickImage = new PickImage() {
            @Override
            public void pickImage() {
                ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                        new ActivityResultCallback<Uri>() {
                            @Override
                            public void onActivityResult(Uri uri) {
                                mapService.setImage(uri);
                            }
                        });
                mGetContent.launch("image/*");
            }
        };

        myEmergencyList = new ArrayList<Emergency>();
        Database.loadEmergencies(new CompleteListener() {
            @Override
            public void OnSuccess() {
            }

            @Override
            public void OnFailure() {
            }
        });

        final Observer<List<Emergency>> myObserver = new Observer<List<Emergency>>() {
            @Override
            public void onChanged(List<Emergency> emergencies) {
                myEmergencyList.clear();
                myEmergencyList.addAll(emergencies);
            }
        };
        MyViewModel.getInstance().getEmergencyLiveData().observe(this, myObserver);

        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        mapService.setImage(uri);
                    }
                });

        mapService = new MapService(getContext(), this, getActivity(), mGetContent, pickImage);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(mapService);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}
