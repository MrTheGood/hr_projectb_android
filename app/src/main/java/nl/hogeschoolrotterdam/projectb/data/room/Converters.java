package nl.hogeschoolrotterdam.projectb.data.room;

import androidx.room.TypeConverter;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by maartendegoede on 23/04/2019.
 * Copyright © 2019 Anass El Mahdaoui, Hicham El Marzgioui, Wesley de Man, Maarten de Goede all rights reserved.
 */
class Converters {
    @TypeConverter
    static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }


    @TypeConverter
    static LatLng latLngFromString(String latlng) {
        String[] ll = latlng.split(":");
        return new LatLng(Double.valueOf(ll[0]), Double.valueOf(ll[1]));
    }

    @TypeConverter
    static String latLngToString(LatLng latLng) {
        return latLng.latitude + ":" + latLng.longitude;
    }


}