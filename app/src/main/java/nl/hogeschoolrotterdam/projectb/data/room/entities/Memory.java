package nl.hogeschoolrotterdam.projectb.data.room.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import nl.hogeschoolrotterdam.projectb.R;

import java.io.IOException;
import java.util.*;

/**
 * Created by maartendegoede on 20/03/2019.
 * Copyright © 2019 Anass El Mahdaoui, Hicham El Marzgioui, Michaël van Asperen, Wesley de Man, Maarten de Goede all rights reserved.
 */
@Entity
public class Memory {

    @NonNull
    @PrimaryKey
    private final String id;
    @NonNull
    private LatLng location;
    @NonNull
    private Date date;
    @NonNull
    private String title;
    @NonNull
    private String description;
    @NonNull
    @Ignore
    private ArrayList<Media> media;
    @DrawableRes
    private int memoryTypeIconId;

    public Memory(@NonNull String id, @NonNull LatLng location, @NonNull Date date, @NonNull String title, @NonNull String description) {
        this(id, location, date, title, description, null, R.drawable.ic_map_adefault);
    }

    public Memory(@NonNull String id, @NonNull LatLng location, @NonNull Date date, @NonNull String title, @NonNull String description, @Nullable ArrayList<Media> media, @DrawableRes int memoryTypeIconId) {
        this.id = id;
        this.location = location;
        this.date = date;
        this.title = title;
        this.description = description;
        this.media = media != null ? media : new ArrayList<Media>();
        this.memoryTypeIconId = memoryTypeIconId;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public LatLng getLocation() {
        return location;
    }

    public void setLocation(@NonNull LatLng location) {
        this.location = location;
    }

    /**
     * When using the date for text, use getDateText() instead
     *
     * @return date
     */
    @NonNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NonNull Date date) {
        this.date = date;
    }

    /**
     * Turns the date into a formatted string
     *
     * @return Formatted days ago text
     */
    @NonNull
    public CharSequence getDateText() {
        return DateUtils.getRelativeTimeSpanString(date.getTime(), Calendar.getInstance().getTimeInMillis(), DateUtils.DAY_IN_MILLIS);
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    /**
     * Use for getting the thumbnail image in lists
     *
     * @return the first image bitmap, or null if not found.
     */
    @Nullable
    public Bitmap getThumbnail() {
        Bitmap bitmap = null;
        for (Media image : media) {
            if (image instanceof Image)
                bitmap = ((Image) image).getImage();
            if (image instanceof Video)
                bitmap = ((Video) image).getThumbnail();

            if (bitmap != null)
                return bitmap;
        }
        return null;
    }

    @NonNull
    public List<Media> getMedia() {
        return media;
    }

    public void _initializeMedia(@NonNull ArrayList<Media> media) {
        this.media = media;
    }

    public void addMedia(Media media) {
        this.media.add(media);
    }

    public void removeMedia(Media media) {
        this.media.remove(media);
    }

    @Nullable
    @Ignore
    public String getCountryName(Context context) {
        Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> listAddresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            if (null != listAddresses && listAddresses.size() > 0) {
                return listAddresses.get(0).getCountryName();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getMemoryTypeIconId() {
        return memoryTypeIconId;
    }

    public void setMemoryTypeIconId(int memoryTypeIconId) {
        this.memoryTypeIconId = memoryTypeIconId;
    }

    public BitmapDescriptor getTypeBitMap(Context context) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, getMemoryTypeIconId());
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


}
