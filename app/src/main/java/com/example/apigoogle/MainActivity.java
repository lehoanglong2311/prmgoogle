package com.example.apigoogle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.example.apigoogle.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    private Geocoder geocoder;
    String address = "Hưng Yên";
    private int type = GoogleMap.MAP_TYPE_NORMAL;
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geocoder = new Geocoder(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
        TextView viewmap = findViewById(R.id.viewmap);
        viewmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });


        if (getIntent().hasExtra("address")){
            address = getIntent().getStringExtra("address");
            Log.d("address",address);


        }

        Button btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchMap.class);
                startActivity(intent);
            }
        });

    }

    private void searchLocation(String address) {
        List<Address> addresses = null;
        LatLng latLng = null;
        try {
            addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && addresses.size() > 0) {
                Address location = addresses.get(0);
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                latLng = new LatLng(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Xử lý lỗi IO và hiển thị thông báo cho người dùng
            Toast.makeText(this, "Lỗi khi tìm địa chỉ.", Toast.LENGTH_SHORT).show();
        }

        if (latLng != null) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title("Vị trí")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            mMap.setMapType(type);
        } else {
            // Xử lý trường hợp không tìm thấy địa chỉ
            Toast.makeText(this, "Không tìm thấy địa chỉ.", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        searchLocation(address);
        findViewById(R.id.Zoom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng hanoi = new LatLng(21.0285,105.8542);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(hanoi,15);
                mMap.animateCamera(cameraUpdate);
            }
        });

//

        findViewById(R.id.showLine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng hanoi = new LatLng(21.0285,105.8542);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(hanoi,15);
                mMap.animateCamera(cameraUpdate);
                mMap.setMapType(type);

                List<LatLng> path = new ArrayList<>();
                path.add(new LatLng(21.0285, 105.8542)); // Điểm A
                path.add(new LatLng(21.0357, 105.8475)); // Điểm B
                // Thêm nhiều điểm khác tại đây

                // Tạo đối tượng PolylineOptions và đặt thuộc tính
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.addAll(path);
                polylineOptions.width(5f); // Độ rộng của đường line
                polylineOptions.color(Color.RED); // Màu sắc của đường line

                // Vẽ đường line trên bản đồ
                Polyline polyline = mMap.addPolyline(polylineOptions);
            }
        });




    }
    private void showPopupMenu(View view) {
        // tạo popmenu mới
        PopupMenu popupMenu = new PopupMenu(this, view);
        // tạo một đối tượng MenuInflater và lấy đối tượng MenuInflater
        MenuInflater inflater = popupMenu.getMenuInflater();
        // lấy dữ liệu từ file menu_main.xml và thêm vafp popupMenu
        inflater.inflate(R.menu.menu_main, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Lấy ID của menu item được chọn
                int id = item.getItemId();

                // So sánh ID với các ID của các mục item mong muốn
                if (id == R.id.menu_item1) {
                    type = GoogleMap.MAP_TYPE_NORMAL;

                } else if (id == R.id.menu_item2) {
                    type = GoogleMap.MAP_TYPE_SATELLITE;

                } else if (id == R.id.menu_item3) {
                    type = GoogleMap.MAP_TYPE_HYBRID;

                } else if (id == R.id.menu_item4) {
                    type = GoogleMap.MAP_TYPE_TERRAIN;

                }
                if (mMap != null) {
                    mMap.setMapType(type);
                }
                return true;
            }
        });
        popupMenu.show();
    }


}
