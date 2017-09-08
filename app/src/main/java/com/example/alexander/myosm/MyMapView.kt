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
        maxZoomLevel=19
        setMultiTouchControls(true)
        setTileSource(object : OnlineTileSourceBase("BaseMap", 0, 19, 256, "", arrayOf(
                "http://a.pkk5.rosreestr.ru/arcgis/rest/services/BaseMaps/BaseMap/MapServer/tile/",
                "http://b.pkk5.rosreestr.ru/arcgis/rest/services/BaseMaps/BaseMap/MapServer/tile/",
                "http://c.pkk5.rosreestr.ru/arcgis/rest/services/BaseMaps/BaseMap/MapServer/tile/"
        )) {
            override fun getTileURLString(aTile: MapTile): String {
                return baseUrl + aTile.zoomLevel + "/" + aTile.y + "/" + aTile.x + mImageFilenameEnding
            }
        })

        val tSourceTitle: OnlineTileSourceBase = object : OnlineTileSourceBase("Anno", 0, 19, 256, "", arrayOf(
                "http://a.pkk5.rosreestr.ru/arcgis/rest/services/BaseMaps/Anno/MapServer/tile/",
                "http://b.pkk5.rosreestr.ru/arcgis/rest/services/BaseMaps/Anno/MapServer/tile/",
                "http://c.pkk5.rosreestr.ru/arcgis/rest/services/BaseMaps/Anno/MapServer/tile/"
        )) {
            override fun getTileURLString(aTile: MapTile): String {
                return baseUrl + aTile.zoomLevel + "/" + aTile.y + "/" + aTile.x + mImageFilenameEnding
            }
        }
        val tProviderTitle = MapTileProviderBasic(context, tSourceTitle)
        val tileOverlayTitle = TilesOverlay(tProviderTitle, context)

        val tSourceArea: OnlineTileSourceBase = object : OnlineTileSourceBase("Area", 0, 19, 256, "", arrayOf(
                "http://a.pkk5.rosreestr.ru/arcgis/rest/services/Cadastre/Cadastre/MapServer/export/?layers=show:0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24&dpi=8&format=png32&transparent=true&bboxSR=3857&imageSR=3857&f=image&size=256,256",
                "http://b.pkk5.rosreestr.ru/arcgis/rest/services/Cadastre/Cadastre/MapServer/export/?layers=show:0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24&dpi=8&format=png32&transparent=true&bboxSR=3857&imageSR=3857&f=image&size=256,256",
                "http://c.pkk5.rosreestr.ru/arcgis/rest/services/Cadastre/Cadastre/MapServer/export/?layers=show:0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24&dpi=8&format=png32&transparent=true&bboxSR=3857&imageSR=3857&f=image&size=256,256"
        )) {
            override fun getTileURLString(aTile: MapTile): String {
                val north = tile2lat(aTile.y, aTile.zoomLevel)
                val south = tile2lat(aTile.y + 1, aTile.zoomLevel)
                val west = tile2lon(aTile.x, aTile.zoomLevel)
                val east = tile2lon(aTile.x + 1, aTile.zoomLevel)

                val smn = SphericalMercator.lat2y(north)
                val sms = SphericalMercator.lat2y(south)
                val smw = SphericalMercator.lon2x(west)
                val sme = SphericalMercator.lon2x(east)

                val s = "$baseUrl&bbox=$smw,$smn,$sme,$sms"
                println(s)
                return s
            }
        }
        val tProviderArea = MapTileProviderBasic(context, tSourceArea)
        val tileOverlayArea = TilesOverlay(tProviderArea, context)

        overlays.add(tileOverlayArea)
        overlays.add(tileOverlayTitle)
        overlays.add(markerFolderOverlay)
        overlays.add(MapEventsOverlay(this))
    }

    override fun longPressHelper(p: GeoPoint): Boolean {
        return false
    }

    override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
//        markerFolderOverlay.presenter.addMarker(p)
        return true
    }

    fun tile2lon(x: Int, z: Int): Double {
        return x / Math.pow(2.0, z.toDouble()) * 360.0 - 180
    }

    fun tile2lat(y: Int, z: Int): Double {
        val n = Math.PI - 2.0 * Math.PI * y.toDouble() / Math.pow(2.0, z.toDouble())
        return Math.toDegrees(Math.atan(Math.sinh(n)))
    }
}
