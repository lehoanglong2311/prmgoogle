package com.example.apigoogle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{
    private int type = GoogleMap.MAP_TYPE_NORMAL;
    private GoogleMap googleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        TextView viewmap = findViewById(R.id.viewmap);
        viewmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        this.googleMap = googleMap;
        LatLng hanoi = new LatLng(21.0285,105.8542);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(hanoi,15);
        googleMap.animateCamera(cameraUpdate);
        googleMap.setMapType(type);

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
        Polyline polyline = googleMap.addPolyline(polylineOptions);
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
                if (googleMap != null) {
                    googleMap.setMapType(type);
                }
                return true;
            }
        });
        popupMenu.show();
    }


}
