package com.example.uptechapp.activity;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.uptechapp.R;
import com.example.uptechapp.api.EmergencyApiService;
import com.example.uptechapp.databinding.FragmentCreateEmergencyBinding;
import com.example.uptechapp.model.Emergency;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateEmergencyFragment extends Fragment {

    private FragmentCreateEmergencyBinding binding;

    private static final String TAG = "CreatingActivity";
    private Button btnChoose, btnShare;
    Intent intent;
    String intent1;
    private ImageView emergencyImg;
    private EditText emergencyLabel, emergencyDescription;
    private Uri uriImage;
    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 1;
    ActivityResultLauncher<String[]> locationPermissionRequest;

    public static Double longitude;
    public static Double latitude;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentCreateEmergencyBinding.inflate(getLayoutInflater());
        init();
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

    private void init() {

//        initbtnChoose = binding.btnChoosePicture;
        btnShare = binding.btnShare;
        emergencyLabel = binding.editTextLabel;
        emergencyDescription = binding.editTextDescription;
        emergencyImg = binding.emergencyImg;

        storageReference = FirebaseStorage.getInstance().getReference("Emergency");

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareEmergency();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uriImage = data.getData();
            emergencyImg.setImageURI(uriImage);
        } else {
            Toast.makeText(getContext(), "Try Later", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareEmergency() {
        if (uriImage != null) {

            int id = 1;

            StorageReference fileReference = storageReference.child(String.valueOf(id) + "/Photo." + getFileExtension(uriImage));

            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri = uri;

                            String url = downloadUri.toString();
                            String[] time = Calendar.getInstance().getTime().toString().split(" ");
                            Log.i("time", "Time" + Arrays.toString(time));

                            Emergency emergency = new Emergency(
                                    "-1",
                                    emergencyLabel.getText().toString(),
                                    emergencyDescription.getText().toString(),
                                    Calendar.getInstance().getTime().toString(),
                                    url,
                                    latitude,
                                    longitude
                            );

                            EmergencyApiService.getInstance().postJson(emergency).enqueue(new Callback<Emergency>() {
                                @Override
                                public void onResponse(@NonNull Call<Emergency> call, @NonNull Response<Emergency> response) {
                                    Log.i(TAG, "Response - " + call.toString());
                                }

                                @Override
                                public void onFailure(@NonNull Call<Emergency> call, @NonNull Throwable t) {
                                    Log.i(TAG, "FAIL - " + t.getMessage());
                                }
                            });

                        }
                    });
                    Navigation.findNavController(getActivity(), R.id.mainFragmentContainer).navigate(R.id.fragment_emergency_feed);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "File was not selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uriImage) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uriImage));
    }

//    private void getLocation() {
//        locationPermissionRequest =
//                registerForActivityResult(new ActivityResultContracts
//                                .RequestMultiplePermissions(), result -> {
//                            Boolean fineLocationGranted = result.getOrDefault(
//                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
//                            Boolean coarseLocationGranted = result.getOrDefault(
//                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
//                            if (fineLocationGranted != null && fineLocationGranted) {
//                                // Precise location access granted.
//                                Toast.makeText(getContext(), "Precise location access granted", Toast.LENGTH_SHORT).show();
//                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
//                                //
//                                Toast.makeText(getContext(), "Only approximate location access granted.", Toast.LENGTH_SHORT).show();
//                            } else {
//                                //
//                                Toast.makeText(getContext(), "No location access granted. Denied", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                );
//        if (!checkLoc()) {
//            locationPermissionRequest.launch(new String[]{
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//            });
//        }
//
//        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
//
//        checkLoc();
//
//        if (!checkLoc()) {
//            return;
//        }
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        fusedLocationClient.getLastLocation()
//                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        // Gt last known location. In some rare situations this can be null.o
//                        if (location != null) {
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                        }
//                    }
//                });
//    }
//
//    boolean checkLoc(){
//        return ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
//                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
//    }
//
//    public static Double getLongitude() {
//        return longitude;
//    }
//
//
//    public static Double getLatitude() {
//        return latitude;
//    }

}
