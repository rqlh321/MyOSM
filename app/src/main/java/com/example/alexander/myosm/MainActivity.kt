package com.example.alexander.myosm

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity

class MainActivity : MvpAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val map = MyMapView(this, mvpDelegate)
        setContentView(map)
    }
}
