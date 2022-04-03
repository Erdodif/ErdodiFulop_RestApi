package phil.petrik.restExam.activities

import android.graphics.Movie
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import phil.petrik.restExam.R
import phil.petrik.restExam.database.City
import phil.petrik.restExam.databinding.ActivityInsertBinding
import phil.petrik.restExam.flow.ViewSwapper
import pl.droidsonroids.gif.GifAnimationMetaData
import pl.droidsonroids.gif.GifDrawable
import java.io.InputStream
import java.lang.Exception
import java.util.*


class InsertActivity : AppCompatActivity() {
    lateinit var bind: ActivityInsertBinding
    lateinit var random: Random
    val gifs = arrayListOf<Int>(
        R.drawable.g1,
        R.drawable.g2,
        R.drawable.g3,
        R.drawable.g4,
        R.drawable.g5,
        R.drawable.g6,
        R.drawable.g7,
        R.drawable.g8,
        R.drawable.g9,
        R.drawable.g10,
        R.drawable.g11,
        R.drawable.g12,
        R.drawable.g13
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityInsertBinding.inflate(layoutInflater)
        setContentView(bind.root)
        disableAdd()
        random = Random()
        bind.add.setOnClickListener { sendAddition() }
        bind.inputLayout.setOnClickListener { stopGif() }
        bind.cityName.editText!!.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                bind.cityName.error = null
            } else {
                validateName()
            }
        }
        bind.cityCountry.editText!!.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                bind.cityCountry.error = null
            } else {
                validateCountry()
            }
        }
        bind.cityPopulation.editText!!.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                bind.cityPopulation.error = null
            } else {
                validatePopulation()
            }
        }
        bind.cityName.editText!!.doOnTextChanged { _, _, _, _ -> enableAdd() }
        bind.cityCountry.editText!!.doOnTextChanged { _, _, _, _ -> enableAdd() }
        bind.cityPopulation.editText!!.doOnTextChanged { _, _, _, _ -> enableAdd() }
    }

    private fun validateName(input: String? = null): Boolean {
        enableAdd()
        val text = input ?: bind.cityName.editText!!.text
        if (!text!!.matches(Regex("^[a-zA-Z]*$"))) {
            bind.cityName.error = "Város formátum nem megfelelő!"
            setRandomGif()
            return false
        }
        bind.cityName.error = null
        return true
    }

    private fun validateCountry(input: String? = null): Boolean {
        enableAdd()
        val text = input ?: bind.cityCountry.editText!!.text
        if (!text!!.matches(Regex("^[a-zA-Z]*\$"))) {
            bind.cityCountry.error = "Ország formátum nem megfelelő!"
            setRandomGif()
            return false
        }
        bind.cityCountry.error = null
        return true
    }

    private fun validatePopulation(input: String? = null): Boolean {
        enableAdd()
        val text = input ?: bind.cityPopulation.editText!!.text
        if (text == null || text.equals("")) {
            bind.cityPopulation.error = "Népesség formátum nem megfelelő!"
            setRandomGif()
            return false
        }
        if (!text.matches(Regex("^[0-9]*$"))) {
            bind.cityPopulation.error = "Népesség formátum nem megfelelő!"
            setRandomGif()
            return false
        }
        try {
            Integer.parseInt(text.toString())
        } catch (e: Exception) {
            bind.cityPopulation.error = "Megadott népesség lehetetlen méretű!"
            setRandomGif()
            return false
        }
        if (Integer.parseInt(text.toString()) > 8000000000) {
            bind.cityPopulation.error = "Népesség meghaladja a föld népességét!"
            setRandomGif()
            return false
        }
        if (Integer.parseInt(text.toString()) < 0) {
            bind.cityPopulation.error = "Egy várost laghatja 0 ember, annál kevesebb nem!"
            setRandomGif()
            return false
        }
        bind.cityPopulation.error = null
        return true
    }

    private fun enableAdd() {
        if (bind.cityName.editText!!.text.isEmpty() ||
            bind.cityCountry.editText!!.text.isEmpty() ||
            bind.cityPopulation.editText!!.text.isEmpty()
        ) {
            bind.add.isEnabled = false
            return
        }
        bind.add.isEnabled = true
    }

    private fun disableAdd() {
        bind.add.isEnabled = false
    }

    private fun sendAddition() {
        if (validatePopulation() && validateCountry() && validateName()) {
            val city = City(
                null,
                bind.cityName.editText!!.text.toString(),
                bind.cityCountry.editText!!.toString(),
                Integer.parseInt(bind.cityPopulation.editText!!.text.toString())
            )
            runBlocking {
                withContext(Dispatchers.IO) {
                    if (City.sendNew(city)) {
                        runOnUiThread {
                            Toast.makeText(
                                this@InsertActivity,
                                "Sikeres küldés",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                this@InsertActivity,
                                "Sikertelen küldés: Ilyen város már benne van a rendszerben!",
                                Toast.LENGTH_LONG
                            ).show()
                            setRandomGif()
                        }
                    }
                }
            }
        }
    }

    private fun setRandomGif(seed: Int = 0) {
        bind.gifAnimation.visibility = View.VISIBLE
        val index = if (seed == 0) random.nextInt(12) + 1 else seed
        val gif = gifs[index]
        bind.gifAnimation.setImageResource(gif)
    }

    private fun stopGif() {
        bind.gifAnimation.visibility = View.INVISIBLE
    }

    override fun onBackPressed() {
        ViewSwapper.swapActivity(this, MainActivity())
    }
}