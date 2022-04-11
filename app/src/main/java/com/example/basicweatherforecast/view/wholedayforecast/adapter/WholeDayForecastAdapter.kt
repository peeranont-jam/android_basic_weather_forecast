package com.example.basicweatherforecast.view.wholedayforecast.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.basicweatherforecast.R
import com.example.basicweatherforecast.data.model.Hourly
import com.example.basicweatherforecast.data.model.TemperatureUnit
import com.example.basicweatherforecast.utils.DataFormatter.toFormatDate

class WholeDayForecastAdapter(
    private val items: Array<Hourly>,
    private val temperatureUnit: TemperatureUnit
) :
    RecyclerView.Adapter<WholeDayForecastAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgWeather: ImageView = view.findViewById(R.id.img_weather)
        val textTemp: TextView = view.findViewById(R.id.tv_temp)
        val textHumidity: TextView = view.findViewById(R.id.tv_humidity)
        val textDatetime: TextView = view.findViewById(R.id.tv_date_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_whole_day_forecast, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]

        val unitSymbol = when (temperatureUnit) {
            TemperatureUnit.Celsius -> "\u2103"
            TemperatureUnit.Fahrenheit -> "\u2109"
        }

        holder.textTemp.text =
            holder.itemView.context.getString(R.string.text_temp_with_unit, data.temp, unitSymbol)
        holder.textHumidity.text =
            holder.itemView.context.getString(R.string.text_humidity, data.humidity)
        holder.textDatetime.text = data.dateTime.toFormatDate()

        Glide.with(holder.itemView.context)
            .load("https://openweathermap.org/img/wn/${data.weather[0].icon}@2x.png")
            .centerCrop()
            .skipMemoryCache(true)
            .into(holder.imgWeather)
    }

    override fun getItemCount() = items.size
}