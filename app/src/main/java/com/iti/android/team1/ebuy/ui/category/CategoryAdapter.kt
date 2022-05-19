package com.iti.android.team1.ebuy.ui.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iti.android.team1.ebuy.R

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
          var image : ImageView = itemView.findViewById(R.id.cat_custom_image)
          var title : TextView = itemView.findViewById(R.id.cat_custom_tv_price)
          var imgFavorite : ImageView = itemView.findViewById(R.id.cat_custom_img_favo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.category_custom_rv_item,null,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.title.text = "55.6"
       holder.imgFavorite.setOnClickListener {
           holder.imgFavorite.setImageResource(R.drawable.fill_heart_image)
       }

    }

    override fun getItemCount(): Int {
        return 8
    }


}