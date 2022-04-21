package com.example.dummyapi

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dummyapi.adapter.MyAdapter
import com.example.dummyapi.repository.Repository
import com.example.dummyapi.utils.Constants.Companion.postsNumber
import kotlinx.android.synthetic.main.activity_tag.*

// getting posts by tag with getPostsByTag method and infinite scroll them

class TagActivity : AppCompatActivity() {
    // ini
    private lateinit var viewModel: MainViewModel
    private val myAdapter by lazy { MyAdapter() }
    lateinit var layoutManager: LinearLayoutManager
    var isLoading = false
    var callouts = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag)
        // Activity is set in incomingIntent
        incomingIntent
    }

    private val incomingIntent: Unit
        private get() {
            val button: Button = findViewById<View>(R.id.mainTag) as Button
            if (intent.hasExtra("tag")) {
                // collecting data from clicked tag
                val tag = intent.getStringExtra("tag")
                button.setText(tag)

                //setting up recyclerView
                setupRecyclerview()

                //setting up layout manager
                layoutManager = LinearLayoutManager(this)
                tagRecyclerView.layoutManager = layoutManager
                if (tag != null) {
                    // forwarding with tag
                    forward(tag, postsNumber, 0)

                    // infinite scroll same as Main Activity
                    tagRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                            super.onScrollStateChanged(recyclerView, newState)
                            if (!recyclerView.canScrollVertically(1)) {
                                if(!isLoading) {
                                    forward(tag,postsNumber*(callouts+1),3000)
                                    callouts++
                                }
                            }
                        }
                    })
                }
            }
        }


    // forward same as Main Activity but we used getPostsByTag
    private fun forward(tag: String,postsNumber: Int, delay: Long) {
        isLoading = true
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        viewModel.getPostsByTag(tag, postsNumber)
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

    // setting up Recyclerview
    private fun setupRecyclerview() {
        tagRecyclerView.adapter = myAdapter
        tagRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}