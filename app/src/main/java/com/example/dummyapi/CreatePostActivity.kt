package com.example.dummyapi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.dummyapi.repository.Repository

// creating post with createPost method
class CreatePostActivity : AppCompatActivity() {
    // init
    private lateinit var rSubmitButton: Button
    private lateinit var rImageUrl: EditText
    private lateinit var rText: EditText
    private lateinit var rTags: EditText
    private lateinit var rLikes: EditText
    private lateinit var rOwner: EditText
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        // Submit Button
        rSubmitButton = findViewById(R.id.submitButton) as Button

        // collected data
        rImageUrl = findViewById(R.id.imageUrl) as EditText
        rLikes = findViewById(R.id.likes) as EditText
        rText = findViewById(R.id.text) as EditText
        rTags = findViewById(R.id.tags) as EditText
        rOwner = findViewById(R.id.owner) as EditText

        rSubmitButton.setOnClickListener {
            // forward
            submitData()
        }
    }

    private fun submitData() {
        // data creation
        val image = rImageUrl.text.toString()
        val text = rText.text.toString()
        val likes = rLikes.text.toString().toInt()
        val tags = rTags.text.toString().split(", ") as ArrayList<String>
        val owner = rOwner.text.toString()

        //forward with createPost
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.createPost(text, image, likes, tags, owner)
        val intent = Intent(this, PostCreatedActivity::class.java)
        this.startActivity(intent)
    }

}