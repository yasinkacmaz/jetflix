package com.yasinkacmaz.jetflix.filter

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
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
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.data.remote.Genre
import com.yasinkacmaz.jetflix.ui.filter.FilterBottomSheet
import com.yasinkacmaz.jetflix.ui.filter.FilterState
import com.yasinkacmaz.jetflix.ui.filter.genres.GenreUiModel
import com.yasinkacmaz.jetflix.ui.filter.option.SortBy
import com.yasinkacmaz.jetflix.ui.filter.option.SortOrder
import com.yasinkacmaz.jetflix.util.getString
import com.yasinkacmaz.jetflix.util.setTestContent
import com.yasinkacmaz.jetflix.util.withRole
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class FilterBottomSheetTest {

    private val genreNames = listOf("Action", "Drama", "Animation", "Comedy")
    private val genres = genreNames.mapIndexed { index, name -> GenreUiModel(genre = Genre(id = index, name = name)) }

    @Test
    fun testFilterComponents() = runComposeUiTest {
        val filterState =
            FilterState(sortOrder = SortOrder.ASCENDING, sortBy = SortBy.REVENUE, includeAdult = true, genres = genres)

        setTestContent {
            Column {
                FilterBottomSheet(filterState = filterState, onDismiss = {}, onFilterStateChanged = {})
            }
        }

        verifySortOrderSelected(filterState.sortOrder)
        verifySortBySelected(filterState.sortBy)
        verifyIncludeAdult(filterState.includeAdult)
        onNodeWithText(getString(SortOrder.DESCENDING.titleResId)).performClick()
        onNodeWithText(getString(SortBy.POPULARITY.titleResId)).performClick()
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

    private fun ComposeUiTest.verifySortOrderSelected(sortOrder: SortOrder) {
        val sortOrderTitle = getString(sortOrder.titleResId)
        onNode(withRole(Role.RadioButton).and(isSelected()).and(hasText(sortOrderTitle))).assertIsDisplayed()
    }

    private fun ComposeUiTest.verifySortBySelected(sortBy: SortBy) {
        val sortByTitle = getString(sortBy.titleResId)
        onNode(withRole(Role.RadioButton).and(isSelected()).and(hasText(sortByTitle))).assertIsDisplayed()
    }

    private fun ComposeUiTest.verifyIncludeAdult(includeAdult: Boolean) {
        val matcher = if (includeAdult) isOn() else isOff()
        includeAdultNode().assert(hasAnyChild(isToggleable().and(matcher)))
    }

    private fun ComposeUiTest.includeAdultNode() = onNodeWithText(getString(R.string.include_adult))
}
