package phil.petrik.restExam.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import phil.petrik.restExam.CityAdapter
import phil.petrik.restExam.R
import phil.petrik.restExam.database.City
import phil.petrik.restExam.databinding.ActivityListResultBinding
import phil.petrik.restExam.flow.ViewSwapper

class ListResultActivity : AppCompatActivity() {
    lateinit var bind :ActivityListResultBinding
    lateinit var cities: List<City>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityListResultBinding.inflate(layoutInflater)
        setContentView(bind.root)
        bind.back.setOnClickListener {
            ViewSwapper.swapActivity(this,MainActivity())
        }
        runBlocking {
            withContext(Dispatchers.IO){
                fillCities()
            }
        }
    }

    suspend fun fillCities(){
        Log.d("ResultState", "started")
        cities = City.getAll()
        Log.d("ResultState", "ended")
        bind.resultsView.adapter = CityAdapter(this, cities)
    }

    override fun onBackPressed(){
        ViewSwapper.swapActivity(this,MainActivity())
    }
}