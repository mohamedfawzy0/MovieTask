package com.movietask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.movietask.model.MoviesResponse
import com.movietask.R
import com.movietask.databinding.ItemMovieRowBinding


class MovieAdapter(var moviesList: MutableList<MoviesResponse.Movie>) :
    RecyclerView.Adapter<MovieAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemMovieRowBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_movie_row, parent, false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val model = moviesList[position]
        holder.bind(model)

    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    inner class MyHolder(private val binding: ItemMovieRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MoviesResponse.Movie) {
            binding.model = movie
        }
    }
}