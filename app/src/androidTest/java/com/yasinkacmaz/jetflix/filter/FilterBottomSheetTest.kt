package com.yasinkacmaz.jetflix.filter

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isOff
import androidx.compose.ui.test.isOn
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.google.accompanist.insets.statusBarsPadding
import com.yasinkacmaz.jetflix.data.Genre
import com.yasinkacmaz.jetflix.ui.filter.FilterContent
import com.yasinkacmaz.jetflix.ui.filter.FilterState
import com.yasinkacmaz.jetflix.ui.filter.option.SortBy
import com.yasinkacmaz.jetflix.ui.filter.option.SortOrder
import com.yasinkacmaz.jetflix.ui.filter.genres.GenreUiModel
import com.yasinkacmaz.jetflix.util.getString
import com.yasinkacmaz.jetflix.util.setTestContent
import com.yasinkacmaz.jetflix.util.withRole
import org.junit.Rule
import org.junit.Test

class FilterBottomSheetTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun should_render_sort_order_as_selected(): Unit = with(composeTestRule) {
        val filterState = FilterState(sortOrder = SortOrder.ASCENDING)

        showFilterBottomSheetContent(filterState)

        verifySortOrder(filterState.sortOrder)
    }

    @Test
    fun should_render_sort_by_as_selected(): Unit = with(composeTestRule) {
        val filterState = FilterState(sortBy = SortBy.REVENUE)

        showFilterBottomSheetContent(filterState)

        verifySortBy(filterState.sortBy)
    }

    @Test
    fun should_render_genres() = with(composeTestRule) {
        val genreNames = listOf("Action", "Drama", "Animation", "Comedy")
        val uiModels = genreNames.map { GenreUiModel(genre = Genre(id = -1, name = it)) }
        val filterState = FilterState(genres = uiModels)

        showFilterBottomSheetContent(filterState)

        genreNames.forEach { genreName ->
            onNodeWithText(genreName).assertIsDisplayed()
        }
    }

    @Test
    fun should_render_selected_genres() = with(composeTestRule) {
        val genreNames = listOf("Action", "Drama", "Animation", "Comedy")
        val uiModels = genreNames.map { GenreUiModel(genre = Genre(id = -1, name = it)) }
        val filterState = FilterState(genres = uiModels, selectedGenreIds = listOf(-1))

        showFilterBottomSheetContent(filterState)

        genreNames.forEach { genreName ->
            onNodeWithText(genreName).assertIsSelected()
        }
    }

    @Test
    fun should_render_include_adult() = with(composeTestRule) {
        val filterState = FilterState(includeAdult = true)

        showFilterBottomSheetContent(filterState)

        verifyIncludeAdult(filterState.includeAdult)
    }

    private fun ComposeContentTestRule.verifySortOrder(sortOrder: SortOrder) {
        val sortOrderTitle = composeTestRule.getString(sortOrder.titleResId)
        onNode(withRole(Role.RadioButton).and(isSelected()).and(hasText(sortOrderTitle))).assertIsDisplayed()
    }

    private fun ComposeContentTestRule.verifySortBy(sortBy: SortBy) {
        val sortByTitle = composeTestRule.getString(sortBy.titleResId)
        onNode(withRole(Role.RadioButton).and(isSelected()).and(hasText(sortByTitle))).assertIsDisplayed()
    }

    private fun ComposeContentTestRule.verifyIncludeAdult(includeAdult: Boolean) {
        val matcher = if (includeAdult) isOn() else isOff()
        onNodeWithText("Include Adult").assert(hasAnyChild(isToggleable().and(matcher)))
    }

    private fun ComposeContentTestRule.showFilterBottomSheetContent(
        filterState: FilterState,
        onFilterStateChanged: (FilterState) -> Unit = { },
        onResetClicked: () -> Unit = { },
        onHideClicked: () -> Unit = { }
    ) = setTestContent {
        Column(Modifier.statusBarsPadding()) {
            FilterContent(filterState, onFilterStateChanged, onResetClicked, onHideClicked)
        }
    }
}
