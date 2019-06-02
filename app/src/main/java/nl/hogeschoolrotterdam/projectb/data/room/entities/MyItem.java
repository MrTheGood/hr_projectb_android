package nl.hogeschoolrotterdam.projectb.data.room.entities;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyItem implements ClusterItem {

    private LatLng position;
    private String title;
    private String snippet;


    public MyItem(LatLng mPosition){
        this.position = mPosition;
    }
    public MyItem(LatLng mPosition, String title, String snippet ){
        this.position = mPosition;
        this.snippet = snippet;
        this.title = title;
    }



    @Override
    public LatLng getPosition() {
        return position;

    }


    public String getTitle() {
        return title;
    }


    public String getSnippet() {
        return snippet;
    }




}