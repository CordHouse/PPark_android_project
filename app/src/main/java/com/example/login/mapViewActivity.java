package com.example.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class mapViewActivity extends AppCompatActivity {

    EditText et_search;
    Button btn_search;
    GoogleMap map;
    MarkerOptions myMarker;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        checkDangerousPermissions();

        et_search = findViewById(R.id.et_search);
        btn_search = findViewById(R.id.btn_search);

        Intent intent = getIntent(); // 1
        String userID = intent.getStringExtra("userID"); // 2
        String userPassword = intent.getStringExtra("userPassword");
        int userReserve = intent.getIntExtra("userReserve",0);
        String state = intent.getStringExtra("state");
        String time = intent.getStringExtra("time");

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mv);
        assert mapFragment != null;
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                map = googleMap;
                LatLng defaultLocation = new LatLng(37.575941, 126.976889);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(defaultLocation);
                markerOptions.title("광화문");
                markerOptions.draggable(true);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(defaultLocation,15);
                map.addMarker(markerOptions);
                map.moveCamera(cameraUpdate);

                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng point) {
                        MarkerOptions mOptions = new MarkerOptions();
                        Double latitude = point.latitude;
                        Double longitude = point.longitude;
                        mOptions.position(new LatLng(latitude, longitude));
                        map.addMarker(mOptions);
                    }
                });

                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        Response.Listener<String> responseListener = new Response.Listener<String>(){
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if(success){
                                        String count = jsonObject.getString("count");
                                        String count2 = jsonObject.getString("count2");
                                        int userReserve1_1 = jsonObject.getInt("userReserve1_1");
                                        int userReserve2_1 = jsonObject.getInt("userReserve2_1");
                                        int userReserve3_1 = jsonObject.getInt("userReserve3_1");
                                        int userReserve4_2 = jsonObject.getInt("userReserve4_2");
                                        int userReserve5_2 = jsonObject.getInt("userReserve5_2");
                                        int userReserve6_2 = jsonObject.getInt("userReserve6_2");
                                        Intent intent = new Intent(mapViewActivity.this,MainActivity_Floor.class);
                                        intent.putExtra("userID",userID); // 3
                                        intent.putExtra("count",count);
                                        intent.putExtra("count2",count2);
                                        intent.putExtra("userReserve",userReserve);
                                        intent.putExtra("state",state);
                                        intent.putExtra("userReserve1_1",userReserve1_1);
                                        intent.putExtra("userReserve2_1",userReserve2_1);
                                        intent.putExtra("userReserve3_1",userReserve3_1);
                                        intent.putExtra("userReserve4_2",userReserve4_2);
                                        intent.putExtra("userReserve5_2",userReserve5_2);
                                        intent.putExtra("userReserve6_2",userReserve6_2);
                                        intent.putExtra("time", time);
                                        intent.putExtra("userPassword", userPassword);
                                        startActivity(intent); // 4 1~4까지 세트로 넘겨줘야함
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("userReserve"), Toast.LENGTH_SHORT).show();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        };
                        countRequest countRequest = new countRequest(userID,responseListener);
                        RequestQueue queue = Volley.newRequestQueue(mapViewActivity.this);
                        queue.add(countRequest);
                        return true;
                    }
                });
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_search.getText().toString().length() > 0) {
                    Location location = getLocationFromAddress(getApplicationContext(), et_search.getText().toString());

                    showCurrentLocation(location);
                }
            }
        });
    }
    private Location getLocationFromAddress(Context context, String address) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        Location resLocation = new Location("");
        try {
            addresses = geocoder.getFromLocationName(address, 10);
            Address addressLoc = addresses.get(0);

            resLocation.setLatitude(addressLoc.getLatitude());
            resLocation.setLongitude(addressLoc.getLongitude());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resLocation;
    }

    private void showCurrentLocation(Location location) {
        LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
        String msg = "Latitutde : " + curPoint.latitude
                + "\nLongitude : " + curPoint.longitude;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        //화면 확대, 숫자가 클수록 확대
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));

        //마커 찍기
        myMarker = new MarkerOptions();
        myMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
        myMarker.title(et_search+"");
        map.addMarker(myMarker);

        Intent intent = getIntent(); // 1
        String userID = intent.getStringExtra("userID"); // 2
        String userPassword = intent.getStringExtra("userPassword");
        String userReserve = intent.getStringExtra("userReserve");
        int state = intent.getIntExtra("state", 0);
        String time = intent.getStringExtra("time");

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                String count = jsonObject.getString("count");
                                String count2 = jsonObject.getString("count2");
                                int userReserve1_1 = jsonObject.getInt("userReserve1_1");
                                int userReserve2_1 = jsonObject.getInt("userReserve2_1");
                                int userReserve3_1 = jsonObject.getInt("userReserve3_1");
                                int userReserve4_2 = jsonObject.getInt("userReserve4_2");
                                int userReserve5_2 = jsonObject.getInt("userReserve5_2");
                                int userReserve6_2 = jsonObject.getInt("userReserve6_2");
                                Intent intent = new Intent(mapViewActivity.this,MainActivity_Floor.class);
                                intent.putExtra("userID",userID); // 3
                                intent.putExtra("count",count);
                                intent.putExtra("count2",count2);
                                intent.putExtra("userReserve",userReserve);
                                intent.putExtra("state",state);
                                intent.putExtra("userReserve1_1",userReserve1_1);
                                intent.putExtra("userReserve2_1",userReserve2_1);
                                intent.putExtra("userReserve3_1",userReserve3_1);
                                intent.putExtra("userReserve4_2",userReserve4_2);
                                intent.putExtra("userReserve5_2",userReserve5_2);
                                intent.putExtra("userReserve6_2",userReserve6_2);
                                intent.putExtra("time",time);
                                intent.putExtra("userPassword", userPassword);
                                startActivity(intent); // 4 1~4까지 세트로 넘겨줘야함
                            }
                            else{
                                Toast.makeText(getApplicationContext(), jsonObject.getString("userReserve"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                countRequest countRequest = new countRequest(userID,responseListener);
                RequestQueue queue = Volley.newRequestQueue(mapViewActivity.this);
                queue.add(countRequest);
                return true;
            }
        });
    }

    //관리자 권한
    private void checkDangerousPermissions() {
        String[] permissions = {
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}

