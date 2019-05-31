package nl.hogeschoolrotterdam.projectb.fragment;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.*;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.maps.android.clustering.ClusterManager;
import nl.hogeschoolrotterdam.projectb.MemoryDetailActivity;
import nl.hogeschoolrotterdam.projectb.MemoryEditActivity;
import nl.hogeschoolrotterdam.projectb.R;
import nl.hogeschoolrotterdam.projectb.data.Database;
import nl.hogeschoolrotterdam.projectb.data.room.entities.MapStateManager;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Memory;
import nl.hogeschoolrotterdam.projectb.data.room.entities.MyItem;
import nl.hogeschoolrotterdam.projectb.util.AnalyticsUtil;
import nl.hogeschoolrotterdam.projectb.util.LocationManager;
import nl.hogeschoolrotterdam.projectb.util.SimpleAnimatorListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maartendegoede on 20/03/2019.
 * Copyright © 2019 Anass El Mahdaoui, Hicham El Marzgioui, Wesley de Man, Maarten de Goede all rights reserved.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static float MIN_TOOLTIP_DISTANCE_MOVED = 50;
    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private EditText mSearchText;
    private LatLng latLng;
    private View tooltip;
    private ClusterManager<MyItem> clusterManager;
    float zoomLevel;
    MapStateManager mapStateManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mSearchText = view.findViewById(R.id.input_search);
        mMapView = view.findViewById(R.id.map);

        tooltip = view.findViewById(R.id.tooltip);
        View tooltipClose = view.findViewById(R.id.tooltipClose);
        tooltipClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeTooltip(true);
            }
        });
        setupTooltipFling();

        boolean hasShownTooltip = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("hasShownMapTooltipTutorial", false);
        if (hasShownTooltip)
            closeTooltip(false);

        if (mMapView != null)
            mMapView.onCreate(null);

        return view;
    }

    private void closeTooltip(boolean animated) {
        if (animated) {
            tooltip.animate()
                    .translationXBy(-tooltip.getWidth())
                    .setInterpolator(new AccelerateInterpolator())
                    .setDuration(300)
                    .setListener(new SimpleAnimatorListener() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            closeTooltip(false);
                        }
                    })
                    .start();
        } else {
            tooltip.setVisibility(View.GONE);
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean("hasShownMapTooltipTutorial", true).apply();
        }
    }

    private void setupTooltipFling() {
        final GestureDetectorCompat gestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
                float distanceX = Math.abs(downEvent.getRawX() - moveEvent.getRawX());

                if (distanceX > MIN_TOOLTIP_DISTANCE_MOVED) {
                    closeTooltip(true);
                }
                return true;
            }
        });
        tooltip.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mMapView != null) {
            mMapView.onResume();
            mMapView.getMapAsync(this);

        }

    }
    @Override
    public void onPause(){
        super.onPause();
        zoomLevel = mGoogleMap.getCameraPosition().zoom;
        MapStateManager mgr = new MapStateManager(getContext());
        mgr.saveMapState(mGoogleMap);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(requireContext());
        mGoogleMap = googleMap;
        //cluster things
        //clusterManager = new ClusterManager<>(this.getContext(),mGoogleMap);
        //googleMap.setOnCameraChangeListener(clusterManager);
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String memoryId = (String) marker.getTag();

                Intent intent = new Intent(getContext(), MemoryDetailActivity.class);
                intent.putExtra("EXTRA_SESSION_ID", memoryId);
                startActivity(intent);

                AnalyticsUtil.selectContent(getContext(), "Map");
            }
        });
        //night mode map
        //MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(getContext(),R.raw.mapstylenight);
        //googleMap.setMapStyle(style);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        MapStateManager mgr = new MapStateManager(getContext());
        final CameraPosition position = mgr.getSavedCameraPosition();

        final GoogleMap map = googleMap;
        LocationManager.getInstance().updateLocation(getActivity(), new LocationManager.OnLocationResultListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onLocationResult(@Nullable Location location) {
                if (location != null) {
                    CameraPosition currentPosition = CameraPosition.builder()
                            .target(new LatLng(location.getLatitude(), location.getLongitude()))
                            .zoom(15)
                            .bearing(0)
                            .tilt(0)
                            .build();
                    if (position == null){
                        map.moveCamera(CameraUpdateFactory.newCameraPosition(currentPosition));
                }else{
                        CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
                        map.moveCamera(update);
                    }
                    map.setMyLocationEnabled(true);
                }
            }
        });
        for (Memory memory : Database.getInstance().getMemories()) {

            Marker marker = googleMap.addMarker(new MarkerOptions().position(memory.getLocation()).title(memory.getTitle())
                    .snippet((String) memory.getDateText())
                    .icon(memory.bitmapDescriptorFromVector(getContext(), memory.getMemoryTypeIconId())));
            marker.setTag(memory.getId());
            //cluster items
            //MyItem myItem = new MyItem(memory.getLocation());
            //clusterManager.addItem(myItem);

        }
        //clusterManager.cluster();
        inity();
    }


    private void inity() {
        mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng laat) {
                latLng = laat;
                Intent i = new Intent(getActivity(), MemoryEditActivity.class);
                i.putExtra("location", latLng);
                startActivity(i);
                AnalyticsUtil.addContent(getContext(), "Map");
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

    private void geoLocate() {
        String searchingString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchingString, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            CameraPosition search = CameraPosition.builder()
                    .target(new LatLng(address.getLatitude(), address.getLongitude()))
                    .zoom(10)
                    .bearing(0)
                    .tilt(0)
                    .build();
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(search));
        }
    }
}
