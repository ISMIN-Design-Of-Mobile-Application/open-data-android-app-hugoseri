package com.ismin.opendataapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ItemActivity : AppCompatActivity() {

    lateinit var backButton : ImageButton
    lateinit var floatingButton : FloatingActionButton
    lateinit var valFromMainActivity: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        valFromMainActivity = intent.getSerializableExtra(ITEM_INFO)

        setContentView(R.layout.activity_item)
        backButton = findViewById(R.id.a_item_btn_img_back)
        backButton.setOnClickListener{view: View? -> (
                this.finish()
                )}
        floatingButton = findViewById(R.id.a_item_fab)
        backButton.setOnClickListener{view: View? -> (
                displayURL("https://www.sncf.com/fr")
                )}
    }

    fun displayURL(url : String) {
        val webpage = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        val chooser = Intent.createChooser(intent, "Ouvrir avec")

        // Verify the intent will resolve to at least one activity
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(chooser)
        }
    }

}
