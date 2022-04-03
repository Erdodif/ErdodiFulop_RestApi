package phil.petrik.restExam.database;

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import phil.petrik.restExam.database.SQLConnector.Companion.serverCall

public class City {
    var id: Int?
        private set
    var name: String
        private set
    var country: String
        private set
    var population: Int
        private set
    val populationString:String
        get(){
            return population.toString()
        }

    constructor(id: Int?, name: String, country: String, population: Int) {
        this.id = id
        this.name = name
        this.country = country
        this.population = population
    }

    constructor(jsonObject: JSONObject) {
        this.id = jsonObject.get("id") as Int?
        this.name = jsonObject.get("nev") as String
        this.country = jsonObject.get("orszag") as String
        this.population = jsonObject.get("lakossag") as Int
    }

    fun toJson(withId: Boolean = false): String {
        val id = if (withId) "\"id\":${this.id}," else ""
        return """{
            |$id 
            |"nev":"${this.name}",
            |"orszag":"${this.country}",
            |"lakossag":${this.population}
            |}""".trimMargin()
    }

    companion object {

        @Suppress("SpellCheckingInspection")
        suspend fun getAll(): ArrayList<City> {
            val response = serverCall("GET", "varosok")
            if (response[0].startsWith("2")) {
                val json = JSONObject(response[1]).getJSONArray("varosok")
                val list = ArrayList<City>()
                for (i in 0 until json.length()) {
                    val item = json.getJSONObject(i)
                    list.add(City(item))
                }
                return list
            } else {
                throw Exception("Error fetching data!")
            }
        }

        suspend fun getById(id: Int): City {
            return City(JSONObject(serverCall("GET", "varosok/$id")[1]))
        }

        suspend fun sendNew(city: City): Boolean {
            val params = JSONObject(city.toJson(withId = false))
            return serverCall("POST", "varosok", params = params)[0].startsWith("2")
        }
    }
}
