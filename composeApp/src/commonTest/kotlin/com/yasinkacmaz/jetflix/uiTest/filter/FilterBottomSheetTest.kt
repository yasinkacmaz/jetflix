package com.yasinkacmaz.jetflix.uiTest.filter

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
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.swipeUp
import com.yasinkacmaz.jetflix.data.remote.Genre
import com.yasinkacmaz.jetflix.ui.filter.FilterBottomSheet
import com.yasinkacmaz.jetflix.ui.filter.FilterState
import com.yasinkacmaz.jetflix.ui.filter.genres.GenreUiModel
import com.yasinkacmaz.jetflix.ui.filter.option.SortBy
import com.yasinkacmaz.jetflix.ui.filter.option.SortOrder
import com.yasinkacmaz.jetflix.uiTest.util.setTestContent
import com.yasinkacmaz.jetflix.uiTest.util.withRole
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class FilterBottomSheetTest {

    private val genreNames = listOf("Action", "Drama", "Animation", "Comedy")
    private val genres = genreNames.mapIndexed { index, name -> GenreUiModel(genre = Genre(id = index, name = name)) }

    @Test
    fun `Should render filter components correctly`() = runComposeUiTest {
        val filterState =
            FilterState(sortOrder = SortOrder.ASCENDING, sortBy = SortBy.REVENUE, includeAdult = true, genres = genres)

        setTestContent {
            FilterBottomSheet(filterState = filterState, onDismiss = {}, onFilterStateChanged = {})
        }

        ensureBottomSheetFullyVisible()
        verifySortBySelected("Revenue")
        verifyIncludeAdult(filterState.includeAdult)
        onNodeWithText("Descending").performClick()
        onNodeWithText("Popularity").performClick()
        includeAdultNode().performClick()
        verifySortOrderSelected("Descending")
        verifySortBySelected("Popularity")
        verifyIncludeAdult(!filterState.includeAdult)

        genreNames.forEach { genreName ->
            onNodeWithText(genreName).assertIsDisplayed()
            onNodeWithText(genreName).assertIsNotSelected()
        }
        onNodeWithText(genreNames.first()).performClick()
        onNodeWithText(genreNames.first()).assertIsSelected()
    }

    private fun ComposeUiTest.ensureBottomSheetFullyVisible() {
        includeAdultNode().performTouchInput { swipeUp() }
    }

    private fun ComposeUiTest.verifySortOrderSelected(sortOrderTitle: String) {
        onNode(withRole(Role.RadioButton).and(isSelected()).and(hasText(sortOrderTitle))).assertIsDisplayed()
    }

    private fun ComposeUiTest.verifySortBySelected(sortByTitle: String) {
        onNode(withRole(Role.RadioButton).and(isSelected()).and(hasText(sortByTitle))).assertIsDisplayed()
    }

    private fun ComposeUiTest.verifyIncludeAdult(includeAdult: Boolean) {
        val matcher = if (includeAdult) isOn() else isOff()
        includeAdultNode().assert(hasAnyChild(isToggleable().and(matcher)))
    }

    private fun ComposeUiTest.includeAdultNode() = onNodeWithText("Include Adult")
}
