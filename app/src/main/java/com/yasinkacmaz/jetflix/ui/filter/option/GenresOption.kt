package com.yasinkacmaz.jetflix.ui.filter.option

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.filter.FilterGrid
import com.yasinkacmaz.jetflix.ui.filter.FilterSectionDivider
import com.yasinkacmaz.jetflix.ui.filter.FilterSectionTitle
import com.yasinkacmaz.jetflix.ui.filter.FilterState
import com.yasinkacmaz.jetflix.ui.filter.genres.GenreUiModel

typealias GenresFilterOption = Pair<List<GenreUiModel>, MutableList<Int>>

class GenresOption(override val defaultValue: GenresFilterOption) : FilterOption<GenresFilterOption> {
    override var currentValue: GenresFilterOption = defaultValue

    private val selectedGenreIds get() = currentValue.second

    override fun modifyFilterState(filterState: FilterState) = filterState.copy(selectedGenreIds = currentValue.second)

    @Composable
    override fun Render(onChanged: () -> Unit) {
        val (genreUiModels, selectedGenreIds) = currentValue
        FilterSectionTitle(painter = rememberVectorPainter(image = Icons.Default.Category), title = R.string.genres)
        FilterGrid(items = genreUiModels) { index, _ ->
            val genreUiModel = genreUiModels[index]
            val genreId = genreUiModel.genre.id
            GenreChip(uiModel = genreUiModel) { selected ->
                selectedGenreIds.removeAll { it == genreId }
                if (selected) {
                    selectedGenreIds.add(genreId)
                }
                currentValue = currentValue.copy(second = selectedGenreIds)
                onChanged()
            }
        }
        FilterSectionDivider()
    }

    @Composable
    private fun GenreChip(uiModel: GenreUiModel, onClicked: (Boolean) -> Unit) {
        val colors = listOf(uiModel.primaryColor, uiModel.secondaryColor)
        val shape = RoundedCornerShape(percent = 50)
        // I've added only genreId and selectedGenreIds to remember because genres does not change.
        // I also am concerned(trying to reduce) about the slot table memory footprint.
        // IMHO this is the fine tuned option of remembering the genre chip selection state.
        var selected by remember(uiModel.genre.id, selectedGenreIds) {
            mutableStateOf(uiModel.genre.id in currentValue.second)
        }
        val animatedColors = List(colors.size) { i ->
            animateColorAsState(if (selected) colors[i] else colors[i].copy(alpha = 0f)).value
        }
        val scale = animateFloatAsState(if (selected) 1.1f else 1f).value
        val modifier = Modifier
            .scale(scale)
            .shadow(animateDpAsState(if (selected) 8.dp else 4.dp).value, shape)
            .background(MaterialTheme.colors.surface)
            .border(2.dp, Brush.horizontalGradient(colors), shape)
            .background(Brush.horizontalGradient(animatedColors), shape)
            .selectable(
                selected,
                onClick = {
                    selected = selected.not()
                    onClicked(selected)
                }
            )
            .padding(horizontal = 10.dp, vertical = 3.dp)

        Text(
            text = uiModel.genre.name.orEmpty(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.body2.copy(fontSize = 17.sp),
            modifier = modifier
        )
    }
}
