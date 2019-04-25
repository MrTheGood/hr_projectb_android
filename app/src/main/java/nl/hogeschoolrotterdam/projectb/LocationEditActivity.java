
package nl.hogeschoolrotterdam.projectb;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import nl.hogeschoolrotterdam.projectb.MainActivity;
import nl.hogeschoolrotterdam.projectb.MemoryDetailActivity;
import nl.hogeschoolrotterdam.projectb.R;
import nl.hogeschoolrotterdam.projectb.data.Database;
import nl.hogeschoolrotterdam.projectb.data.Memory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by maartendegoede on 20/03/2019.
 * Copyright © 2019 Anass El Mahdaoui, Hicham El Marzgioui, Michaël van Asperen, Wesley de Man, Maarten de Goede all rights reserved.
 */
public class LocationEditActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    MapView mMapView;
    private EditText mSearchText;
    private static final String TAG = "Location";
    private Button button;
    LatLng latLng;


    private void inity() {
        mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener(){

            @Override
            public void onMapLongClick(LatLng Longl) {
                latLng = Longl;
                setMarker(latLng);

            }
        });
        mSearchText = (EditText) findViewById(R.id.input_search);
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
            Log.d(TAG, "locate adress:" + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
            CameraPosition search = CameraPosition.builder().target(new LatLng(address.getLatitude(),
                    address.getLongitude())).zoom(10).bearing(0).tilt(0).build();
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(search));
            latLng =  new LatLng(address.getLatitude(), address.getLongitude());
            setMarker(latLng);




        }
    }
    private void setMarker(LatLng latLng){
        mGoogleMap.clear();;
        MarkerOptions options = new MarkerOptions().position(latLng);
        mGoogleMap.addMarker(options);

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_edit);
        button = findViewById(R.id.memory_save_location_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",latLng);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

            }
        });
        mMapView = findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

    }





    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this);
        googleMap.setMapType(googleMap.MAP_TYPE_NORMAL);
        mGoogleMap = googleMap;
        inity();
    }
}




