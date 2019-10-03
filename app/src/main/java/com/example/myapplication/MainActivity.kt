package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import com.example.myapplication.databinding.ActivityMainBinding
import com.rbalda.androidutils.liveevent.LiveEvent

class MainActivity : AppCompatActivity() {

    val liveEvent:LiveEvent<LiveEvent.Event> by lazy {
        LiveEvent<LiveEvent.Event>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding:ActivityMainBinding = setContentView(this,R.layout.activity_main)
        binding.handler = Handler()
        liveEvent.observe(this, Observer {
            when(it){
                is LiveEvent.SuccessEvent -> Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                is LiveEvent.FailureEvent -> Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
                is LiveEvent.LoadingEvent -> Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
            }
        })
    }

    inner class Handler {
        fun success(){
            liveEvent.postValue(LiveEvent.SuccessEvent(null))
        }

        fun failure(){
            liveEvent.postValue(LiveEvent.FailureEvent(null))
        }

        fun loading(){
            liveEvent.postValue(LiveEvent.LoadingEvent(null))
        }
    }
}
