package com.example.alexander.myosm

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.osmdroid.util.GeoPoint

@InjectViewState
class MarkersPresenter : MvpPresenter<MarkersView>() {
    private val points = ArrayList<GeoPoint>()

    init {
        viewState.addMarkers(points)
    }

    fun addMarker(geoPoint: GeoPoint) {
        points.add(geoPoint)
        viewState.addMarker(geoPoint)
    }

    fun replace(oldPoint: GeoPoint, newPoint: GeoPoint) {
        val i = points.indexOf(oldPoint)
        points.removeAt(i)
        points.add(i, newPoint)
    }

}