package com.example.lab345unichnu.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.lab345unichnu.R
import com.example.lab345unichnu.data.local.models.Device
import java.io.File

class CustomPhoneAdapter(
    private val dataSet: List<Device>,
    private val context: Context
) : RecyclerView.Adapter<CustomPhoneAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val price: TextView
        val image: ImageView

        init {
            // Define click listener for the ViewHolder's View
            name = view.findViewById(R.id.model_name)
            price = view.findViewById(R.id.txtview_price)
            image = view.findViewById(R.id.image_view)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycle_view_row, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = dataSet[position]

        viewHolder.name.text = item.name
        viewHolder.price.text = "${item.type}"

        item.image?.let { imagePath ->
            Glide.with(context)
                .load(File(imagePath)) // Передаємо файл напряму
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) // Кешування
                .into(viewHolder.image)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}