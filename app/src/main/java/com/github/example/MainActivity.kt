package com.github.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn_test).setOnClickListener {
            /*Timber.d("ddd")
            Timber.i("iii")
            Timber.v("vvv")
            Timber.e("eee")
            Timber.w("www")*/
            try {
                val s = 5/0
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e)
            }

//            val s2 = 6/0
        }
    }
}