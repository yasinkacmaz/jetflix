package com.yasinkacmaz.jetflix.ui.navigation

const val ARG_MOVIE_ID = "MOVIE_ID"
const val ARG_INITIAL_PAGE = "initialPage"
const val ARG_PERSON_ID = "personId"

enum class Screen(val route: String) {
    MOVIES("movies"),
    DETAIL("movie/{$ARG_MOVIE_ID}/detail"),
    IMAGES("movie/{$ARG_MOVIE_ID}/images?$ARG_INITIAL_PAGE={$ARG_INITIAL_PAGE}"),
    CAST("movie/{$ARG_MOVIE_ID}/cast"),
    CREW("movie/{$ARG_MOVIE_ID}/crew"),
    PROFILE("profile/{$ARG_PERSON_ID}"),
    ;

    fun createPath(vararg args: Any): String {
        var route = route
        require(args.size == route.argumentCount) {
            "Provided ${args.count()} parameters, was expected ${route.argumentCount} parameters!"
        }
        route.arguments().forEachIndexed { index, matchResult ->
            route = route.replace(matchResult.value, args[index].toString())
        }
        return route
    }
}
