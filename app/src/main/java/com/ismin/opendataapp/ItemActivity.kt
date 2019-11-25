package com.ismin.opendataapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton

class ItemActivity : AppCompatActivity() {

    lateinit var backButton : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        backButton = findViewById(R.id.a_item_btn_img_back)
        backButton.setOnClickListener{view: View? -> (
                this.finish()
                )}
    }
}
