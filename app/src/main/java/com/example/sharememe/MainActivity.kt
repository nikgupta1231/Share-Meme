package com.example.sharememe

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val MEME_API = "https://meme-api.herokuapp.com/gimme"
    }

    private var memeUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        share_meme_btn.setOnClickListener { shareMeme() }
        next_meme_btn.setOnClickListener { nextMeme() }

        //loading first meme
        nextMeme()
    }

    private fun shareMeme() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey there, check out this meme $memeUrl")
        startActivity(Intent.createChooser(intent, "Share meme via..."))
    }

    private fun nextMeme() {
        progress_circular.visibility = View.VISIBLE

        // Request a string response from the provided URL.
        val request = JsonObjectRequest(Request.Method.GET, MEME_API, null,
                { response ->
                    memeUrl = response.getString("url")
                    Glide.with(this).load(memeUrl).listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            progress_circular.visibility = View.GONE
//                            Toast.makeText(, "Unable to load meme, try again!", Toast.LENGTH_LONG).show()
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            progress_circular.visibility = View.GONE
                            return false
                        }
                    }).into(meme_image_view)
                },
                { Toast.makeText(this, "Unable to load meme, try again!", Toast.LENGTH_LONG).show() })

        // Add the request to the RequestQueue.
        ApiNetworkQueue.getInstance(this).addToRequestQueue(request)
    }

}