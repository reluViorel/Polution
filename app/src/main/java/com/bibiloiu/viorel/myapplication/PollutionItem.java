package com.bibiloiu.viorel.myapplication;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class PollutionItem implements ClusterItem {
    private final LatLng mPosition;
    BitmapDescriptor icon;
    String title;
    String snippet;

    public PollutionItem(BitmapDescriptor ic,Double lat , Double lng,String tit ,String sni) {
        mPosition = new LatLng(lat, lng);
        icon = ic;
        title = tit;
        snippet = sni;
    }

    public BitmapDescriptor getIcon() {
        return icon;
    }

    public void setIcon(BitmapDescriptor icon) {
        this.icon = icon;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

}