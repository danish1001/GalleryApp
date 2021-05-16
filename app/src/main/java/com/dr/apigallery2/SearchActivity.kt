package com.dr.apigallery2

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dr.apigallery2.ApiPosts.JsonPlaceHolder
import com.dr.apigallery2.ApiPosts.Post
import com.dr.apigallery2.Recycler.RecyclerAdapter
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Cancellable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class SearchActivity : AppCompatActivity() {

    // Declaring Instances
    lateinit var recyclerView: RecyclerView
    var recyclerAdapter: RecyclerView.Adapter<*>? = null
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var searchUrls: ArrayList<String>
    lateinit var relativeLayout: RelativeLayout
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var searchView: androidx.appcompat.widget.SearchView
    lateinit var progressBar: ProgressBar
    private var disposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        // fetching instances to their views

        recyclerView = findViewById<View>(R.id.searchRecyclerView) as RecyclerView
        recyclerView.setHasFixedSize(false)
        layoutManager = GridLayoutManager(this@SearchActivity, 3)
        recyclerView.layoutManager = layoutManager
        searchUrls = mutableListOf<String>() as ArrayList<String>
        recyclerAdapter = RecyclerAdapter(this@SearchActivity, searchUrls)
        recyclerView.adapter = recyclerAdapter
        relativeLayout = findViewById<View>(R.id.searchRelativeLayout) as RelativeLayout
        swipeRefreshLayout = findViewById<View>(R.id.searchRefresh) as SwipeRefreshLayout
        searchView = findViewById<View>(R.id.searchSearchText) as SearchView
        progressBar = findViewById<View>(R.id.progress1) as ProgressBar



        // refresh layout listener

        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            searchUrls.clear()
            (recyclerAdapter as RecyclerAdapter).notifyDataSetChanged()

            var handler: Handler = Handler()
            handler.postDelayed(Runnable {
                swipeRefreshLayout.isRefreshing = false
            }, 5000)
        })

        // Observable query text and Debounce Operator rxJava

        var observableQueryText: Observable<String> = Observable.create(object: ObservableEmitter<String>,
            @io.reactivex.rxjava3.annotations.NonNull ObservableOnSubscribe<String> {
            override fun subscribe(emitter: ObservableEmitter<String>?) {
                searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (emitter != null) {
                            if(!emitter.isDisposed) {
                                emitter.onNext(query)
                            }
                        }
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (emitter != null) {
                            if(!emitter.isDisposed) {
                                emitter.onNext(newText)
                            }
                        }
                        return false
                    }
                })
            }

            override fun isDisposed(): Boolean {
                TODO("Not yet implemented")
            }

            override fun tryOnError(t: Throwable?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onComplete() {
                TODO("Not yet implemented")
            }

            override fun setCancellable(c: Cancellable?) {
                TODO("Not yet implemented")
            }

            override fun setDisposable(d: Disposable?) {
                TODO("Not yet implemented")
            }

            override fun serialize(): ObservableEmitter<String> {
                TODO("Not yet implemented")
            }

            override fun onNext(value: String?) {
                TODO("Not yet implemented")
            }

            override fun onError(error: Throwable?) {
                TODO("Not yet implemented")
            }

        })
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())

        // subscribe to observableQueryText

        observableQueryText.subscribe(object :Observer<String> {
            override fun onComplete() {
                TODO("Not yet implemented")
            }

            override fun onSubscribe(d: Disposable?) {
                disposable.add(d)
            }

            override fun onNext(t: String?) {

                // onChange in searchbar: send query to the server to fetch data
                sendRequestToServer(t.toString());
            }

            override fun onError(e: Throwable?) {
                TODO("Not yet implemented")
            }

        })

    }


    fun sendRequestToServer(data: String) {
        checkData(data)           // check if searchBar is not empty or blank

        runOnUiThread(Runnable {                // progress bar is visible now
            progressBar.visibility = View.VISIBLE
        })

    }

    fun checkData(data: String) {

        var Url: String = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=" +
                "6f102c62f41998d151e5a1b48713cf13&format=json&nojsoncallback=1&extras=url_s&text=" + data

        if(data.isBlank()) {
            showSnackBar(Url, data)
        } else {
            LoadMoreData(Url)
        }

    }

    // check Internet Connection

    public fun checkConnection(): (Boolean) {
        var manager: ConnectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = manager.activeNetworkInfo

        if(activeNetwork != null) {
            return true
        }
        return false;
    }

    // snackBar function

    public fun showSnackBar(Url: String, data: String) {
        val snackbar = Snackbar.make(relativeLayout, "Enter something", 1000)
            .setTextColor(Color.RED)
            .setAction("Retry") {
                if (data.isBlank()) {
                    showSnackBar(Url, data)
                } else {
                    checkData(Url)
                }
            }
        snackbar.show()
    }

    // fetching api and getting URl data

    fun LoadMoreData(URL: String) {

        searchUrls.clear()
        runOnUiThread(Runnable {
            (recyclerAdapter as RecyclerAdapter).notifyDataSetChanged()
        })

        // retrofit
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.flickr.com")
            .build()

        val jsonPlaceHolder = retrofit.create(JsonPlaceHolder::class.java)
        var Url: String = URL
        var call: Call<Post> = jsonPlaceHolder.getPosts(Url);

        call.enqueue(object : Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                runOnUiThread(Runnable {
                    Toast.makeText(this@SearchActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
                })
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {

                // adding urls of images to arrayList of URLS

                for (myPost in response.body()?.getPhotos()?.getPhotosArrays()!!) {
                    myPost.getUrl_s()?.let { searchUrls.add(it) }
                }

                runOnUiThread(Runnable {
                    (recyclerAdapter as RecyclerAdapter).notifyDataSetChanged()
                })
            }

        })
        runOnUiThread(Runnable {
            var handler: Handler = Handler()
            handler.postDelayed(Runnable {
                progressBar.visibility = View.INVISIBLE
            }, 3000)
        })
    }
}