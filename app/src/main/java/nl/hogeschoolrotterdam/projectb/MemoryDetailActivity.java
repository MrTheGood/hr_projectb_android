package nl.hogeschoolrotterdam.projectb;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import nl.hogeschoolrotterdam.projectb.adapter.ViewPagerAdapter;
import nl.hogeschoolrotterdam.projectb.data.Database;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Image;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Media;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Memory;
import nl.hogeschoolrotterdam.projectb.fragment.ShareFragment;
import nl.hogeschoolrotterdam.projectb.util.AnalyticsUtil;

import java.util.ArrayList;

public class MemoryDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView memoryTitleTextView;
    TextView memoryDatetextView;
    TextView memoryDescriptionTextView;
    Toolbar toolbar;
    TextView viewPagerIndicator;
    ViewPager2 viewPager2;
    ViewPagerAdapter mediaAdapter;
    Memory memory;
    GoogleMap mGoogleMap;
    MapView mMapView;
    LatLng latLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(WhibApp.getInstance().getThemeId());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_detail);

        toolbar = findViewById(R.id.toolbar);
        memoryTitleTextView = findViewById(R.id.memoryTitleTextView);
        memoryDatetextView = findViewById(R.id.memoryDatetextView);
        memoryDescriptionTextView = findViewById(R.id.memoryDescriptionTextView);
        viewPager2 = findViewById(R.id.viewPager2);
        viewPagerIndicator = findViewById(R.id.viewPager_indicator);

        mediaAdapter = new ViewPagerAdapter();
        viewPager2.setAdapter(mediaAdapter);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Drawable homeAsUpIndicator = ContextCompat.getDrawable(this, R.drawable.ic_action_close); // Workaround for a bug in MaterialComponents
        getSupportActionBar().setHomeAsUpIndicator(WhibApp.getInstance().tintDrawable(homeAsUpIndicator));

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                viewPagerIndicator.setText((position + 1) + "/" + memory.getMedia().size());
            }
        });

        mMapView = findViewById(R.id.map7);
        mMapView.onCreate(null);
        mMapView.onResume();
        mMapView.getMapAsync(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Database database = Database.getInstance();
        String sessionId = getIntent().getStringExtra("EXTRA_SESSION_ID");
        memory = database.findMemory(sessionId);
        mediaAdapter.setMedia(memory.getMedia());

        memoryDatetextView.setText(memory.getDateText());
        memoryTitleTextView.setText(memory.getTitle());
        memoryDescriptionTextView.setText(memory.getDescription());
        if (memory.getMedia().size() > 0) {
            viewPagerIndicator.setText((viewPager2.getCurrentItem() + 1 + "/" + memory.getMedia().size()));
            //imageView.setImageDrawable(memory.getThumbnail().getImage()); // for thumbnail in list
            // if (media instanceOf Image) imageView.setImageDrawable(media.getImage()); // for image in swipable detail list

        } else {
            viewPagerIndicator.setText("0/0");
        }
        mMapView.onResume();
        mMapView.getMapAsync(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar.inflateMenu(R.menu.menu_main_memorydetail_top);
        WhibApp.getInstance().tintMenuItems(menu); // Workaround for a Material Components bug

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        ArrayList<Image> images = new ArrayList<>();
        for (Media m : memory.getMedia()) {
            if (m instanceof Image) {
                images.add((Image) m);
            }
        }
        switch (item.getItemId()) {
            case R.id.shareBtn:
                //todo: implement facebook share library
                //todo: make sure all shares work and filter out any not-working apps
                //todo: share all content including date, images, videos, title, description

                new ShareFragment(memory).show(getSupportFragmentManager(), "shareSheetDialog");
                return true;
            case R.id.deleteBtn:
                new AlertDialog.Builder(MemoryDetailActivity.this)
                        .setTitle("Delete Memory")
                        .setMessage("Are you sure you want to delete this memory? This action cannot be undone.")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AnalyticsUtil.deleteContent(MemoryDetailActivity.this);
                                Database.getInstance().deleteMemory(memory);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;
            case R.id.editBtn:
                Intent intent = new Intent(MemoryDetailActivity.this, MemoryEditActivity.class);
                intent.putExtra("ID", memory.getId());
                startActivity(intent);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setMarker(LatLng latLng) {
        CameraPosition search = CameraPosition.builder().target(latLng)
                .zoom(15).bearing(0).tilt(0).build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(search));
        mGoogleMap.clear();
        MarkerOptions options = new MarkerOptions().position(latLng);
        mGoogleMap.addMarker(options);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        latLng = memory.getLocation();
        mGoogleMap = googleMap;
        setMarker(latLng);

    }

}
