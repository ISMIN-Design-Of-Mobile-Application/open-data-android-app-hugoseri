package com.ismin.opendataapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.squareup.picasso.Picasso
import com.squareup.picasso.Callback

class ItemActivity : AppCompatActivity() {

    lateinit var backButton: ImageButton
    lateinit var floatingButton: FloatingActionButton
    lateinit var valFromMainActivity: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        valFromMainActivity = intent.getSerializableExtra(ITEM_INFO) as Item

        setContentView(R.layout.activity_item)
        backButton = findViewById(R.id.a_item_btn_img_back)
        backButton.setOnClickListener { _: View? ->
            (
                    this.finish()
                    )
        }
        floatingButton = findViewById(R.id.a_item_fab)
        floatingButton.setOnClickListener { _: View? ->
            (
                    displayURL(valFromMainActivity.url)
                    )
        }

        val title: TextView = findViewById(R.id.a_item_txt_titre)
        title.text = valFromMainActivity.titre
        val position: TextView = findViewById(R.id.a_item_txt_ville)
        position.text = valFromMainActivity.lieux
        val typeImg: ImageView = findViewById(R.id.a_item_img_type_data)
        typeImg.setImageResource(getIconFromType(valFromMainActivity.type))
        val desc: TextView = findViewById(R.id.a_item_txt_description)
        desc.text = valFromMainActivity.legende
        val periode: TextView = findViewById(R.id.a_item_txt_periode)
        periode.text = valFromMainActivity.periode
        val archived: TextView = findViewById(R.id.a_item_txt_archived_location)
        archived.text = valFromMainActivity.lieux_de_conservation
        val previewImg : ImageButton = findViewById(R.id.a_item_img_preview)
        val picasso = Picasso.Builder(this)
            .listener { _, _, e -> e.printStackTrace() }
            .build()
        picasso.load(Uri.parse("https://en.wikipedia.org/wiki/Wikipedia#/media/File:Wikipedia-logo-v2.svg"))
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_file_unknown)
            .into(previewImg)
        previewImg.setOnClickListener{_ : View? ->(
            displayURL(valFromMainActivity.url)
            )}
    }

    fun displayURL(url: String) {
        val webpage = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        val chooser = Intent.createChooser(intent, "Ouvrir avec")

        // Verify the intent will resolve to at least one activity
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(chooser)
        }
    }

    fun getIconFromType(type: String): Int {
        when (type) {
            "PDF" -> return R.drawable.ic_file_pdf
            "JPEG" -> return R.drawable.ic_file_jpg
            "PNG" -> return R.drawable.ic_file_png
            else -> return R.drawable.ic_file_unknown
        }
    }
}
