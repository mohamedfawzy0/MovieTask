package com.movietask.model

data class MoviesResponse(
    var errorMessage: String = "",
    var items: ArrayList<Movie> = ArrayList()
) {
    data class Movie(
        var crew: String = "",
        var fullTitle: String = "",
        var id: String = "",
        var imDbRating: String = "",
        var imDbRatingCount: String = "",
        var image: String = "",
        var rank: String = "",
        var title: String = "",
        var year: String = ""
    )
}

