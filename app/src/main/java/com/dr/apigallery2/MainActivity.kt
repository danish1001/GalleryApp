package com.dr.apigallery2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dr.apigallery2.ApiPosts.JsonPlaceHolder
import com.dr.apigallery2.ApiPosts.Post
import com.dr.apigallery2.Recycler.RecyclerAdapter
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Url


class MainActivity : AppCompatActivity() {


    lateinit var recyclerView: RecyclerView
    var recyclerAdapter: RecyclerView.Adapter<*>? = null
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var urls: ArrayList<String>
    lateinit var relativeLayout: RelativeLayout
    var currentItems: Int = 0
    var scrollOutItems: Int = 0
    var totalItems: Int = 0
    var isScrolling: Boolean = false
    var pageNumber: Int = 1
    lateinit var progressBar: ProgressBar
    lateinit var swipeRefreshLayout: SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        recyclerView.setHasFixedSize(false)
        layoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = layoutManager
        urls = mutableListOf<String>() as ArrayList<String>
        recyclerAdapter = RecyclerAdapter(this@MainActivity, urls)
        recyclerView.adapter = recyclerAdapter
        relativeLayout = findViewById<View>(R.id.relativeLayout) as RelativeLayout
        progressBar = findViewById<View>(R.id.progress) as ProgressBar
        swipeRefreshLayout = findViewById<View>(R.id.refresh) as SwipeRefreshLayout

        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            pageNumber = 1
            urls.clear()
            (recyclerAdapter as RecyclerAdapter).notifyDataSetChanged()
            LoadData()

            var handler: Handler = Handler()
            handler.postDelayed(Runnable {
                swipeRefreshLayout.isRefreshing = false
            }, 3000)
        })


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                currentItems = (layoutManager as GridLayoutManager).childCount
                totalItems = (layoutManager as GridLayoutManager).itemCount
                scrollOutItems = (layoutManager as GridLayoutManager).findFirstVisibleItemPosition()

                // fetch data if condition is met

                if(isScrolling && (totalItems == currentItems + scrollOutItems)) {
                    isScrolling = false
                    pageNumber++;
                    LoadData()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }
        })

        LoadData()

    }

    // button to enter another Activity

    @SuppressLint("LongLogTag")
    public fun searchClicked(view: View) {
        var intent: Intent = Intent(this@MainActivity, SearchActivity::class.java)
        startActivity(intent)
    }

    // fetching api and getting URl data

    fun LoadMoreData() {

        progressBar.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.flickr.com")
            .build()

        val jsonPlaceHolder = retrofit.create(JsonPlaceHolder::class.java)

        var Url: String = "https://api.flickr.com/services/rest/?method=flickr.photos" +
                ".getRecent&per_page=35&page="+ pageNumber +"&api_key=6f102c62f41998d151e" +
                "5a1b48713cf13&format=json&nojsoncallback=1&extras=url_s"

        var call: Call<Post> = jsonPlaceHolder.getPosts(Url);

        call.enqueue(object : Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                runOnUiThread(Runnable {
                    Toast.makeText(this@MainActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
                })
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {

                // adding urls of images to arrayList of URLS

                for (myPost in response.body()?.getPhotos()?.getPhotosArrays()!!) {
                    myPost.getUrl_s()?.let { urls.add(it) }
                }

                recyclerAdapter?.notifyDataSetChanged()

                var handler: Handler = Handler()
                handler.postDelayed(Runnable {
                    progressBar.visibility = View.INVISIBLE
                }, 3000)

            }

        })

    }

    // checking Internet then fetching api
    public fun LoadData() {
        if(checkConnection()) {
            LoadMoreData()
        } else {
            showSnackBar()
        }
    }

    // checking Internet Connection
    public fun checkConnection(): (Boolean) {
        var manager: ConnectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = manager.activeNetworkInfo

        if(activeNetwork != null) {
            return true
        }

        return false;
    }

    // snackbar

    fun showSnackBar() {
        val snackbar = Snackbar.make(relativeLayout, "No internet connection !", 1000000)
            .setTextColor(Color.RED)
            .setAction("Retry") {
                if (!checkConnection()) {
                    showSnackBar()
                } else {
                    LoadData()
                }
            }
        snackbar.show()
    }

}