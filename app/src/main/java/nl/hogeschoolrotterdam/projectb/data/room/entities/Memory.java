package nl.hogeschoolrotterdam.projectb.data.room.entities;

import android.graphics.Bitmap;
import android.text.format.DateUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by maartendegoede on 20/03/2019.
 * Copyright © 2019 Anass El Mahdaoui, Hicham El Marzgioui, Michaël van Asperen, Wesley de Man, Maarten de Goede all rights reserved.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Memory {

    @NonNull
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
    private ArrayList<Media> media;

    public Memory(@NonNull String id, @NonNull LatLng location, @NonNull Date date, @NonNull String title, @NonNull String description, @Nullable ArrayList<Media> media) {
        this.id = id;
        this.location = location;
        this.date = date;
        this.title = title;
        this.description = description;
        this.media = media != null ? media : new ArrayList<Media>();
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
        for (Media image : media) {
            if (image instanceof Image)
                return ((Image) image).getImage();
        }
        return null;
    }

    @NonNull
    public List<Media> getMedia() {
        return media;
    }

    public void addMedia(Media media) {
        this.media.add(media);
    }

    public void removeMedia(Media media) {
        this.media.remove(media);
    }
}
