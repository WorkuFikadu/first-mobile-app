package com.example.showcaseapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showcaseapp.data.DataRepository
import com.example.showcaseapp.data.ShowcaseFeature
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class CounterState(
    val count: Int = 0,
    val clicks: Int = 0
)

data class MainScreenUiData(
    val features: List<ShowcaseFeature> = emptyList(),
    val counter: CounterState = CounterState(),
    val greetingName: String = "",
    val selectedTags: Set<String> = setOf("Compose", "Material 3"),
    val isBookmarked: Boolean = false
)

sealed interface MainScreenUiState {
    object Loading : MainScreenUiState
    data class Error(val throwable: Throwable) : MainScreenUiState
    data class Success(val data: MainScreenUiData) : MainScreenUiState
}

class MainScreenViewModel(private val dataRepository: DataRepository) : ViewModel() {

    private val _counter = MutableStateFlow(CounterState())
    private val _greetingName = MutableStateFlow("")
    private val _selectedTags = MutableStateFlow(setOf("Compose", "Material 3"))
    private val _isBookmarked = MutableStateFlow(false)

    val uiState: StateFlow<MainScreenUiState> = combine(
        dataRepository.features,
        _counter,
        _greetingName,
        _selectedTags,
        _isBookmarked
    ) { features, counter, name, tags, isBookmarked ->
        MainScreenUiState.Success(
            MainScreenUiData(
                features = features,
                counter = counter,
                greetingName = name,
                selectedTags = tags,
                isBookmarked = isBookmarked
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainScreenUiState.Loading
    )

    fun incrementCounter(step: Int = 1) {
        _counter.update { it.copy(count = it.count + step, clicks = it.clicks + 1) }
    }

    fun decrementCounter(step: Int = 1) {
        _counter.update { it.copy(count = maxOf(0, it.count - step), clicks = it.clicks + 1) }
    }

    fun resetCounter() {
        _counter.update { it.copy(count = 0) }
    }

    fun onGreetingNameChange(name: String) {
        _greetingName.value = name
    }

    fun toggleTag(tag: String) {
        _selectedTags.update { tags ->
            if (tag in tags) tags - tag else tags + tag
        }
    }

    fun toggleBookmark() {
        _isBookmarked.update { !it }
    }
}
