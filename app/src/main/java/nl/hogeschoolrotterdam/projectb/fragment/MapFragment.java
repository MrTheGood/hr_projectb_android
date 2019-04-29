package nl.hogeschoolrotterdam.projectb.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import nl.hogeschoolrotterdam.projectb.MemoryDetailActivity;
import nl.hogeschoolrotterdam.projectb.MemoryEditActivity;
import nl.hogeschoolrotterdam.projectb.R;
import nl.hogeschoolrotterdam.projectb.data.Database;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Memory;
import nl.hogeschoolrotterdam.projectb.util.LocationManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maartendegoede on 20/03/2019.
 * Copyright © 2019 Anass El Mahdaoui, Hicham El Marzgioui, Michaël van Asperen, Wesley de Man, Maarten de Goede all rights reserved.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    private EditText mSearchText;
    LatLng latLng;




    private void inity() {
        mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng laat) {
                latLng = laat;
                Intent i = new Intent(getActivity(), MemoryEditActivity.class);
                i.putExtra("location",latLng );
                startActivity(i);


            }
        });
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
    private void geoLocate(){

        String searchingString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchingString,1);


        }catch (IOException e){
        }
        if (list.size() > 0){
            Address address = list.get(0);
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
            CameraPosition search = CameraPosition.builder().target(new LatLng(address.getLatitude(),
                    address.getLongitude())).zoom(10).bearing(0).tilt(0).build();
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(search));




        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);

        // texten setten
        mSearchText = mView.findViewById(R.id.input_search);

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(requireContext());
        mGoogleMap = googleMap;
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String memoryId = (String) marker.getTag();
                Intent intent = new Intent(getContext(), MemoryDetailActivity.class);
                intent.putExtra("EXTRA_SESSION_ID", memoryId);
                startActivity(intent);
            }
        });
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        final GoogleMap map = googleMap;
        LocationManager.getInstance().updateLocation(getActivity(), new LocationManager.OnLocationResultListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onLocationResult(@Nullable Location location) {
                if (location != null) {
                    CameraPosition currentPosition = CameraPosition.builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(15).bearing(0).tilt(0).build();
                    map.moveCamera(CameraUpdateFactory.newCameraPosition(currentPosition));
                    map.setMyLocationEnabled(true);
                }
            }
        });
        for (Memory memorie : Database.getInstance().getMemories()) {
            Marker marker = googleMap.addMarker(new MarkerOptions().position(memorie.getLocation()).title(memorie.getTitle()).snippet((String) memorie.getDateText()));
            marker.setTag(memorie.getId());
        }
        inity();
    }


}
