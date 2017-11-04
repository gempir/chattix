package com.gempir.chattix

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_chat.*

class Chat : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


//        addChannel.setOnClickListener {
//            Toast.makeText(this, "Button Pressed", Toast.LENGTH_LONG).show()
//        }
    }
}
