package com.iti.android.team1.ebuy.ui.home

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.iti.android.team1.ebuy.R

class HomeViewPagerAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    data class Card(val id: Int)

    val items = mutableListOf<Card>().apply {
        repeat(10) { add(Card(it)) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.ad_layout, parent, false)
        ) {}
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Empty
    }

}