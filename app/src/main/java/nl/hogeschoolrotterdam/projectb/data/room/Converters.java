package nl.hogeschoolrotterdam.projectb.data.room;

import androidx.room.TypeConverter;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by maartendegoede on 23/04/2019.
 * Copyright © 2019 Anass El Mahdaoui, Hicham El Marzgioui, Wesley de Man, Maarten de Goede all rights reserved.
 */
@SuppressWarnings("WeakerAccess")
public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }


    @TypeConverter
    public static LatLng latLngFromString(String latlng) {
        String[] ll = latlng.split(":");
        return new LatLng(Double.valueOf(ll[0]), Double.valueOf(ll[1]));
    }

    @TypeConverter
    public static String latLngToString(LatLng latLng) {
        return latLng.latitude + ":" + latLng.longitude;
    }


}