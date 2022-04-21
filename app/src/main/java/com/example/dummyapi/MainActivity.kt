package com.example.dummyapi

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dummyapi.adapter.MyAdapter
import com.example.dummyapi.repository.Repository
import com.example.dummyapi.utils.Constants.Companion.pageStart
import com.example.dummyapi.utils.Constants.Companion.postsNumber
import kotlinx.android.synthetic.main.activity_main.*

/* Main Activity:
    -button to start CreatePostActivity
    -posts called with retrofit in main activity's view model
    -infinite scroll posts
 */
open class MainActivity : AppCompatActivity() {
    //init
    private lateinit var viewModel: MainViewModel
    private val myAdapter by lazy { MyAdapter() }
    lateinit var layoutManager: LinearLayoutManager
    var isLoading = false
    var callouts = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerview()

        // go to CreatePostActivity after button click
        createNewPostButton.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            this.startActivity(intent)
        }

        // init layout
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        /* getting main posts
            pageStart = 0
            postNumber = 5
            can be changed from utils.Constants
         */
        forward(pageStart,postsNumber, 0)


        // infinite scroll
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if(!isLoading) {
                        // forward after reaching the button
                        forward(pageStart,postsNumber*(callouts+1),3000)
                        // adding one callout
                        callouts++
                    }
                }
            }
        })

    }


    // forward ini
    private fun forward(pageNumber: Int, postsNumber: Int, delay: Long) {
        // ini repository.Repository
        isLoading = true
        val repository = Repository()

        // ini viewModelFactory
        val viewModelFactory = MainViewModelFactory(repository)

        // forwading
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getPosts(pageNumber, postsNumber)
        viewModel.myResponse.observe(this, Observer { response ->
            if(response.isSuccessful){
                // delay to stop spam
                Handler().postDelayed({
                    isLoading = false
                }, delay)
                myAdapter.setData(this,R.layout.row_layout, response.body()?.data!!)
            }else {
                Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
            }
        })

    }

    //setting up recyclerview
    private fun setupRecyclerview() {
        recyclerView.adapter = myAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }


}