
package nl.hogeschoolrotterdam.projectb;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wesley de man on 24/04/2019.
 * Copyright © 2019 Anass El Mahdaoui, Hicham El Marzgioui, Wesley de Man, Maarten de Goede all rights reserved.
 */
public class LocationEditActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    MapView mMapView;
    private EditText mSearchText;
    private Button button;
    LatLng latLng;


    private void inity() {
        mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng Longl) {
                latLng = Longl;
                setMarker(latLng);

            }
        });
        mSearchText = findViewById(R.id.input_search);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    geoLocate();
                }
                return false;
            }
        });
    }

    private void geoLocate() {
        String searchingString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchingString, 1);


        } catch (IOException e) {
        }
        if (list.size() > 0) {
            Address address = list.get(0);
            latLng = new LatLng(address.getLatitude(), address.getLongitude());
            setMarker(latLng);


        }
    }

    private void setMarker(LatLng latLng) {
        float zoomLevel = mGoogleMap.getCameraPosition().zoom;
        CameraPosition search = CameraPosition.builder().target(latLng
                ).zoom(zoomLevel).bearing(0).tilt(0).build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(search));
        mGoogleMap.clear();
        MarkerOptions options = new MarkerOptions().position(latLng);
        mGoogleMap.addMarker(options);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(WhibApp.getInstance().getThemeId());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_edit);
        button = findViewById(R.id.memory_save_location_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", latLng);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            }
        });
        mMapView = findViewById(R.id.map);
        mMapView.onCreate(null);
        mMapView.onResume();
        mMapView.getMapAsync(this);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        latLng = (LatLng) getIntent().getExtras().get("location");
        mGoogleMap = googleMap;
        setMarker(latLng);
        inity();
    }
}




