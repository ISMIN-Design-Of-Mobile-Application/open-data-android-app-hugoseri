package com.ismin.opendataapp

import android.app.ActionBar
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.OkHttp3Downloader

import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


class ItemActivity : AppCompatActivity() {

    lateinit var backButton: ImageButton
    lateinit var floatingButton: FloatingActionButton
    lateinit var valFromMainActivity: Item


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        valFromMainActivity = intent.getSerializableExtra(ITEM_INFO) as Item

        setContentView(R.layout.activity_item)

        floatingButton = findViewById(R.id.a_item_fab)
        floatingButton.setImageBitmap(textAsBitmap(valFromMainActivity.type))
        floatingButton.setOnClickListener { _: View? -> (
            displayURL(valFromMainActivity.url)
        )}

        val title: TextView = findViewById(R.id.a_item_txt_titre)
        title.text = valFromMainActivity.titre
        val position: TextView = findViewById(R.id.a_item_txt_ville)
        position.text = valFromMainActivity.lieux
        /*
        val typeImg: ImageView = findViewById(R.id.a_item_img_type_data)
        typeImg.setImageResource(getIconFromType(valFromMainActivity.type))
        */
        val desc: TextView = findViewById(R.id.a_item_txt_description)
        desc.text = valFromMainActivity.legende
        val periode: TextView = findViewById(R.id.a_item_txt_periode)
        periode.text = valFromMainActivity.periode
        val archived: TextView = findViewById(R.id.a_item_txt_archived_location)
        archived.text = valFromMainActivity.lieux_de_conservation
        val previewImg : ImageButton = findViewById(R.id.a_item_img_preview)
        //Initialize a ok http downloader to use timeouts
        val client = OkHttpClient().newBuilder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120,TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()
        val picasso = Picasso.Builder(this)
            .listener { _, _, e -> e.printStackTrace() }
            .downloader(OkHttp3Downloader(client))
            .build()
        picasso.load(Uri.parse(valFromMainActivity.apercu))
            .placeholder(R.drawable.ic_launcher_background)
            .resize(1024, 800)
            .centerCrop()
            .onlyScaleDown()
            .error(R.drawable.ic_file_unknown)
            .into(previewImg)
        previewImg.setOnClickListener{_ : View? ->(
            displayURL(valFromMainActivity.url)
            )}
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == android.R.id.home){
            this.finish()
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean{
        return true
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
