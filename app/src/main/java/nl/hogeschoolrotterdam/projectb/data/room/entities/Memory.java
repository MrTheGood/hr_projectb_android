package nl.hogeschoolrotterdam.projectb.data.room.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    private int memoryType;

    public Memory(@NonNull String id, @NonNull LatLng location, @NonNull Date date, @NonNull String title, @NonNull String description) {
        this(id, location, date, title, description, null, R.drawable.ic_map_adefault);
    }

    public Memory(@NonNull String id, @NonNull LatLng location, @NonNull Date date, @NonNull String title, @NonNull String description, @Nullable ArrayList<Media> media, int memoryType) {
        this.id = id;
        this.location = location;
        this.date = date;
        this.title = title;
        this.description = description;
        this.media = media != null ? media : new ArrayList<Media>();
        this.memoryType = memoryType;
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
                if (listAddresses.get(0).getCountryName() != "" && listAddresses.get(0).getCountryName() != null){
                return listAddresses.get(0).getCountryName();
            }}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    public int getMemoryTypeIconId() {
        switch (getMemoryType()) {
            case 1:
                return R.drawable.ic_map_architecture;
            case 2:
                return R.drawable.ic_map_baby;
            case 3:
                return R.drawable.ic_map_bar;
            case 4:
                return R.drawable.ic_map_beach;
            case 5:
                return R.drawable.ic_map_birthday;
            case 6:
                return R.drawable.ic_map_cafe;
            case 7:
                return R.drawable.ic_map_camera;
            case 8:
                return R.drawable.ic_map_cart;
            case 9:
                return R.drawable.ic_map_city;
            case 10:
                return R.drawable.ic_map_flagged;
            case 11:
                return R.drawable.ic_map_games;
            case 12:
                return R.drawable.ic_map_golf;
            case 13:
                return R.drawable.ic_map_home;
            case 14:
                return R.drawable.ic_map_images;
            case 15:
                return R.drawable.ic_map_iron_throne;
            case 16:
                return R.drawable.ic_map_job;
            case 17:
                return R.drawable.ic_map_landscape;
            case 18:
                return R.drawable.ic_map_money;
            case 19:
                return R.drawable.ic_map_mood_extremely_happy;
            case 20:
                return R.drawable.ic_map_mood_extremely_sad;
            case 21:
                return R.drawable.ic_map_mood_happy;
            case 22:
                return R.drawable.ic_map_mood_sad;
            case 23:
                return R.drawable.ic_map_mood_very_happy;
            case 24:
                return R.drawable.ic_map_mood_very_sad;
            case 25:
                return R.drawable.ic_map_music;
            case 26:
                return R.drawable.ic_map_painting;
            case 27:
                return R.drawable.ic_map_party;
            case 28:
                return R.drawable.ic_map_pets;
            case 29:
                return R.drawable.ic_map_pool;
            case 30:
                return R.drawable.ic_map_restaurant;
            case 31:
                return R.drawable.ic_map_school;
            case 32:
                return R.drawable.ic_map_sexy;
            case 33:
                return R.drawable.ic_map_shield;
            case 34:
                return R.drawable.ic_map_spa;
            case 35:
                return R.drawable.ic_map_speakers;
            case 36:
                return R.drawable.ic_map_starred;
            case 37:
                return R.drawable.ic_map_store;
            case 38:
                return R.drawable.ic_map_styles;
            case 39:
                return R.drawable.ic_map_teathers;
            case 40:
                return R.drawable.ic_map_tourist;
            case 41:
                return R.drawable.ic_map_want_to_go;
            case 42:
                return R.drawable.ic_map_world;
            default:
                return R.drawable.ic_map_adefault;
        }
    }

    public int getMemoryType() {
        return memoryType;
    }

    public void setMemoryType(int memoryType) {
        this.memoryType = memoryType;
    }

    public BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        if (vectorDrawableResourceId == R.drawable.ic_map_adefault){
            Drawable background = ContextCompat.getDrawable(context, getIconBackground());
            Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
            background.setBounds((background.getIntrinsicWidth() - vectorDrawable.getIntrinsicWidth())/2 ,( background.getIntrinsicHeight() - vectorDrawable.getIntrinsicHeight()) /3
                    , background.getIntrinsicWidth(), background.getIntrinsicHeight());
            vectorDrawable.setBounds(55, 35, vectorDrawable.getIntrinsicWidth() + 50, vectorDrawable.getIntrinsicHeight() + 25);
            Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            background.draw(canvas);
            vectorDrawable.draw(canvas);
            return BitmapDescriptorFactory.fromBitmap(bitmap);
        }
        else{
        Drawable background = ContextCompat.getDrawable(context, getIconBackground());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        background.setBounds((background.getIntrinsicWidth() - vectorDrawable.getIntrinsicWidth())/2 ,( background.getIntrinsicHeight() - vectorDrawable.getIntrinsicHeight()) /3
                , background.getIntrinsicWidth(), background.getIntrinsicHeight());
        vectorDrawable.setBounds(55, 35, vectorDrawable.getIntrinsicWidth() + 50, vectorDrawable.getIntrinsicHeight() + 25);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }}
    public int getIconBackground(){
        if (getMemoryType() <=10){
            return R.drawable.ic_backround_red_pointer;

        } else if (getMemoryType() >10 && getMemoryType() <=20){
            return R.drawable.ic_backround_purple_pointer;
                }
        else if (getMemoryType() >20 && getMemoryType()<=30){
            return R.drawable.ic_backround_orange_pointer;
              }
        else return R.drawable.ic_backround_blue_pointer;
    }
    public String getYear() {
        Calendar c = Calendar.getInstance();
        c.setTime(getDate());
        String year = c.get(Calendar.YEAR) + "";
        return year;

    }


}
