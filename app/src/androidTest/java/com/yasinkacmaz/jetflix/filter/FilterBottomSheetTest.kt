package com.yasinkacmaz.jetflix.filter

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
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
import androidx.compose.ui.test.performClick
import com.yasinkacmaz.jetflix.data.remote.Genre
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.filter.FilterBottomSheetContent
import com.yasinkacmaz.jetflix.ui.filter.FilterState
import com.yasinkacmaz.jetflix.ui.filter.genres.GenreUiModel
import com.yasinkacmaz.jetflix.ui.filter.option.SortBy
import com.yasinkacmaz.jetflix.ui.filter.option.SortOrder
import com.yasinkacmaz.jetflix.util.getString
import com.yasinkacmaz.jetflix.util.setTestContent
import com.yasinkacmaz.jetflix.util.withRole
import org.junit.Rule
import org.junit.Test

class FilterBottomSheetTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val genreNames = listOf("Action", "Drama", "Animation", "Comedy")
    private val genres = genreNames.mapIndexed { index, name -> GenreUiModel(genre = Genre(id = index, name = name)) }

    @Test
    fun testFilterComponents(): Unit = with(composeTestRule) {
        val filterState =
            FilterState(sortOrder = SortOrder.ASCENDING, sortBy = SortBy.REVENUE, includeAdult = true, genres = genres)

        setTestContent {
            Column {
                FilterBottomSheetContent(filterState) {}
            }
        }

        verifySortOrderSelected(filterState.sortOrder)
        verifySortBySelected(filterState.sortBy)
        verifyIncludeAdult(filterState.includeAdult)
        onNodeWithText(composeTestRule.getString(SortOrder.DESCENDING.titleResId)).performClick()
        onNodeWithText(composeTestRule.getString(SortBy.POPULARITY.titleResId)).performClick()
        includeAdultNode().performClick()
        verifySortOrderSelected(SortOrder.DESCENDING)
        verifySortBySelected(SortBy.POPULARITY)
        verifyIncludeAdult(!filterState.includeAdult)

        genreNames.forEach { genreName ->
            onNodeWithText(genreName).assertIsDisplayed()
            onNodeWithText(genreName).assertIsNotSelected()
        }
        onNodeWithText(genreNames.first()).performClick()
        onNodeWithText(genreNames.first()).assertIsSelected()
    }

    private fun ComposeContentTestRule.verifySortOrderSelected(sortOrder: SortOrder) {
        val sortOrderTitle = composeTestRule.getString(sortOrder.titleResId)
        onNode(withRole(Role.RadioButton).and(isSelected()).and(hasText(sortOrderTitle))).assertIsDisplayed()
    }

    private fun ComposeContentTestRule.verifySortBySelected(sortBy: SortBy) {
        val sortByTitle = composeTestRule.getString(sortBy.titleResId)
        onNode(withRole(Role.RadioButton).and(isSelected()).and(hasText(sortByTitle))).assertIsDisplayed()
    }

    private fun ComposeContentTestRule.verifyIncludeAdult(includeAdult: Boolean) {
        val matcher = if (includeAdult) isOn() else isOff()
        includeAdultNode().assert(hasAnyChild(isToggleable().and(matcher)))
    }

    private fun ComposeContentTestRule.includeAdultNode() =
        onNodeWithText(composeTestRule.getString(R.string.include_adult))
}
