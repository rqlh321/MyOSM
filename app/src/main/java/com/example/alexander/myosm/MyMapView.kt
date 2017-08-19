package com.example.alexander.myosm

import android.annotation.SuppressLint
import android.content.Context
import com.arellomobile.mvp.MvpDelegate
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay


@SuppressLint("ViewConstructor")
class MyMapView(context: Context, parentDelegate: MvpDelegate<*>) : MapView(context), MapEventsReceiver {
    private val markerFolderOverlay = MarkerFolderOverlay(this, parentDelegate)

    init {
        overlays.add(markerFolderOverlay)
        overlays.add(MapEventsOverlay(this))
    }

    override fun longPressHelper(p: GeoPoint): Boolean {
        return false
    }

    override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
        markerFolderOverlay.presenter.addMarker(p)
        return true
    }


}
