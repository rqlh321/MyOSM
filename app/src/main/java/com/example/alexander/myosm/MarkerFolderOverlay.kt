package com.example.alexander.myosm

import com.arellomobile.mvp.MvpDelegate
import com.arellomobile.mvp.presenter.InjectPresenter
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.FolderOverlay
import org.osmdroid.views.overlay.Marker

class MarkerFolderOverlay(private val mapView: MyMapView, parentDelegate: MvpDelegate<*>) : FolderOverlay(), MarkersView {
    private val mMvpDelegate: MvpDelegate<MarkerFolderOverlay> = MvpDelegate(this)

    @InjectPresenter
    lateinit var presenter: MarkersPresenter

    init {
        mMvpDelegate.setParentDelegate(parentDelegate, MarkerFolderOverlay::class.java.simpleName)
        mMvpDelegate.onCreate()
        mMvpDelegate.onAttach()
    }

    override fun onDetach(mapView: MapView) {
        super.onDetach(mapView)
        mMvpDelegate.onSaveInstanceState()
        mMvpDelegate.onDetach()
    }

    override fun addMarkers(geoPoints: ArrayList<GeoPoint>) {
        geoPoints.forEach {
            items.add(getMarker(it))
        }
        mapView.invalidate()
    }

    override fun addMarker(geoPoint: GeoPoint) {
        items.add(getMarker(geoPoint))
        mapView.invalidate()
    }

    private fun getMarker(geoPoint: GeoPoint): Marker {
        val marker = Marker(mapView)
        marker.position = geoPoint
        marker.isDraggable = true
        marker.setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
            lateinit var oldPoint: GeoPoint

            override fun onMarkerDragEnd(marker: Marker) {
                presenter.replace(oldPoint, marker.position)
            }

            override fun onMarkerDragStart(marker: Marker) {
                oldPoint = marker.position
            }

            override fun onMarkerDrag(marker: Marker) {
            }
        })
        return marker
    }

}