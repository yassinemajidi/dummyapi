package com.example.dummyapi

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dummyapi.adapter.MyPostAdapter
import com.example.dummyapi.model.main.PostData
import com.example.dummyapi.repository.Repository
import kotlinx.android.synthetic.main.activity_post.*

// getting post with getPost method
class PostActivity : AppCompatActivity() {
    //ini
    private lateinit var viewModel: MainViewModel
    private val MyPostAdapter by lazy { MyPostAdapter() }
    lateinit var layoutManager: LinearLayoutManager
    var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        // activity on intent
        incomingIntent
    }

    private val incomingIntent: Unit
        private get() {
            if (intent.hasExtra("post_id")) {
                // collecting data from clicked post
                val postId = intent.getStringExtra("post_id")

                // setting up recyclerView
                setupRecyclerview()
                layoutManager = LinearLayoutManager(this)
                postRecyclerView.layoutManager = layoutManager
                if (postId != null) {
                    // forward
                    forward(postId, 0)
                }
            }
        }

    private fun forward(postId: String, delay: Long) {
        // same as Main but we use getPost method
        isLoading = true
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        viewModel.getPost(postId)
        viewModel.myPostResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {
                // delay to stop spam
                Handler().postDelayed({
                    isLoading = false
                }, delay)
                val post: List<PostData> = listOf(response.body()) as List<PostData>
                MyPostAdapter.setPostData(this,R.layout.post_layout, post)
            } else {
                Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Recyclerview ini
    private fun setupRecyclerview() {
        postRecyclerView.adapter = MyPostAdapter
        postRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}
