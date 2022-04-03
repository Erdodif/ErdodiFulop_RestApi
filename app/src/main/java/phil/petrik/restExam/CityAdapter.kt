package phil.petrik.restExam

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import phil.petrik.restExam.database.City
import phil.petrik.restExam.databinding.ListItemBinding

class CityAdapter(context: Context, var values: List<City>) : ArrayAdapter<City>(context, -1, values){
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = ListItemBinding.inflate(inflater)
        rowView.city = values[position]
        return rowView.root
    }
}