package com.example.alexander.myosm

import android.annotation.SuppressLint
import android.content.Context
import com.arellomobile.mvp.MvpDelegate
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.MapTile
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.TilesOverlay

@SuppressLint("ViewConstructor")
class MyMapView(context: Context, parentDelegate: MvpDelegate<*>) : MapView(context), MapEventsReceiver {
    private val markerFolderOverlay = MarkerFolderOverlay(this, parentDelegate)

    init {

        setTileSource(object : OnlineTileSourceBase("BaseMap", 0, 18, 256, "", arrayOf("http://a.pkk5.rosreestr.ru/arcgis/rest/services/BaseMaps/BaseMap/MapServer/tile/")) {
            override fun getTileURLString(aTile: MapTile): String {
                return baseUrl + aTile.zoomLevel + "/" + aTile.y + "/" + aTile.x + mImageFilenameEnding
            }
        })

        val tSourceTitle: OnlineTileSourceBase = object : OnlineTileSourceBase("Anno", 0, 18, 256, "", arrayOf("http://a.pkk5.rosreestr.ru/arcgis/rest/services/BaseMaps/Anno/MapServer/tile/")) {
            override fun getTileURLString(aTile: MapTile): String {
                return baseUrl + aTile.zoomLevel + "/" + aTile.y + "/" + aTile.x + mImageFilenameEnding
            }
        }
        val tProviderTitle = MapTileProviderBasic(context, tSourceTitle)
        val tileOverlayTitle = TilesOverlay(tProviderTitle, context)
        overlays.add(tileOverlayTitle)
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
