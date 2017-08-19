package com.example.alexander.myosm

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import org.osmdroid.util.GeoPoint

interface MarkersView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun addMarkers(geoPoints: ArrayList<GeoPoint>)

    @StateStrategyType(SkipStrategy::class)
    fun addMarker(geoPoint: GeoPoint)
}