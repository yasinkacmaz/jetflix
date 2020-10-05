package com.yasinkacmaz.jetflix.ui.main.moviedetail

import com.yasinkacmaz.jetflix.data.Genre

data class MovieDetail(
   val id: Int,
   val title: String,
   val originalTitle: String,
   val overview: String,
   val backdropUrl: String,
   val posterUrl: String,
   val genres: List<Genre>,
   val releaseDate: String,
   val voteAverage: Double,
   val voteCount: Int,
   val duration: Int
)
