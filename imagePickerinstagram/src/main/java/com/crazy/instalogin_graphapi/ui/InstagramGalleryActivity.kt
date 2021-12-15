package com.crazy.instalogin_graphapi.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crazy.instalogin_graphapi.R
import com.crazy.instalogin_graphapi.model.MediaData
import com.crazy.instalogin_graphapi.model.MediaResponse
import com.crazy.instalogin_graphapi.network.ApiClient
import com.crazy.instalogin_graphapi.network.NetworkService
import com.crazy.instalogin_graphapi.common.Constant
import com.crazy.instalogin_graphapi.common.PreferenceHelper
import com.crazy.instalogin_graphapi.common.PreferenceHelper.set
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InstagramGalleryActivity : AppCompatActivity() {
    private var gridLayoutManager: GridLayoutManager? = null
    private var photoList: ArrayList<MediaData>? = ArrayList()
    private var instagramGalleryAdapter: InstagramGalleryAdapter? = null
    private var galleryRecyclerView: RecyclerView? = null
    private var addPhotoButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_instagram)
        initView()
        initListener()
        getGalleryList()
        setupRecyclerview()
    }

    private fun initListener() {
        addPhotoButton?.setOnClickListener {
            val intent = Intent()
            intent.putExtra(
                KEY_RESULT_INSTAGRAM,
                instagramGalleryAdapter?.selectedMediaData?.mediaUrl
            )
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun initView() {
        galleryRecyclerView = findViewById(R.id.galleryRecyclerView)
        addPhotoButton = findViewById(R.id.addPhotoButton)
    }

    private fun getGalleryList() {

        val call =
            ApiClient.getClient(Constant.GRAPH_API, this)?.create(NetworkService::class.java)
        call?.media(
            PreferenceHelper.defaultPrefs(this@InstagramGalleryActivity)
                .getString(PreferenceHelper.USER_ID, "") ?: "",
            PreferenceHelper.defaultPrefs(this@InstagramGalleryActivity)
                .getString(PreferenceHelper.ACCESS_TOKEN, "")  ?: "",
            "media_url,media_type"
        )!!.enqueue(object : Callback<MediaResponse?> {


            override fun onFailure(call: Call<MediaResponse?>, t: Throwable) {
                Toast.makeText(
                    this@InstagramGalleryActivity,
                    getString(R.string.err_get_photos),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: Call<MediaResponse?>,
                response: Response<MediaResponse?>
            ) {

                if(response.code() == 400) {
                    Toast.makeText(
                        this@InstagramGalleryActivity,
                        getString(R.string.msg_invalid_token),
                        Toast.LENGTH_SHORT
                    ).show()
                    PreferenceHelper.defaultPrefs(this@InstagramGalleryActivity).set(PreferenceHelper.ACCESS_TOKEN, "")
                    finish()
                    return
                }

                val filteredMediaList: List<MediaData>? =
                    response.body()?.data?.filter { s -> s.mediaType == Constant.MEDIA_TYPE_IMAGE }

                instagramGalleryAdapter?.updateData(filteredMediaList as java.util.ArrayList<MediaData>?)
            }
        })

    }

    private fun setupRecyclerview() {
        gridLayoutManager = GridLayoutManager(applicationContext, 3)
        galleryRecyclerView?.layoutManager = gridLayoutManager
        instagramGalleryAdapter = InstagramGalleryAdapter(applicationContext, photoList)
        galleryRecyclerView?.adapter = instagramGalleryAdapter

    }

}