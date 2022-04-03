package phil.petrik.restExam.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import phil.petrik.restExam.databinding.ActivityMainBinding
import phil.petrik.restExam.flow.ViewSwapper

class MainActivity : AppCompatActivity() {
    lateinit var bind: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        bind.toListing.setOnClickListener {
            ViewSwapper.swapActivity(this, ListResultActivity())
        }
        bind.toInsert.setOnClickListener {
            ViewSwapper.swapActivity(this, InsertActivity())
        }
    }
}