package com.onopry.weatherapp_avito.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.onopry.domain.model.forecast.LocalitySearch
import com.onopry.weatherapp_avito.R

class CitySearchAdapter(
    private val mContext: Context,
    @LayoutRes private val resourceId: Int,
    private val searchCitiesList: List<LocalitySearch>
) : ArrayAdapter<LocalitySearch>(mContext, resourceId, searchCitiesList), Filterable {

    private class Holder(itemView: View) {
        val nameLocality: TextView = itemView.findViewById(R.id.locationNameTv)
    }

    override fun getCount() = searchCitiesList.size

    override fun getItem(position: Int) = searchCitiesList[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row: View
        val holder: Holder
        if (convertView == null) {
            val inflater = LayoutInflater.from(mContext)
            row = inflater.inflate(R.layout.item_search_response, parent, false)
            holder = Holder(row)
            row.tag = holder
        } else {
            row = convertView
            holder = row.tag as Holder
        }

        val city = searchCitiesList[position]
        holder.nameLocality.text = "${city.country}, ${city.name}"
        return row
    }

    override fun getFilter() = object : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var suggestions = emptyList<LocalitySearch>()
            if (!constraint.isNullOrBlank()) {
                suggestions = searchCitiesList.distinctBy { locality ->
                    Pair(first = locality.name, second = locality.country)
                }
            }
            return FilterResults().also {
                it.values = suggestions
                it.count = suggestions.size
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            clear()
            val res = (results?.values as? MutableList<LocalitySearch>)
            addAll(res ?: emptyList())
            notifyDataSetChanged()
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            val item = resultValue as LocalitySearch
            return "${item.country}, ${item.name.uppercase()}"
        }
    }
}