package com.example.showcaseapp.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class ShowcaseFeature(
    val id: String,
    val title: String,
    val description: String,
    val icon: String
)

interface DataRepository {
    val features: Flow<List<ShowcaseFeature>>
}

class DefaultDataRepository : DataRepository {
    override val features: Flow<List<ShowcaseFeature>> = flow {
        emit(
            listOf(
                ShowcaseFeature(
                    id = "compose",
                    title = "Jetpack Compose",
                    description = "Modern declarative UI toolkit that makes UI development fast, intuitive, and concise.",
                    icon = "🎨"
                ),
                ShowcaseFeature(
                    id = "material3",
                    title = "Material 3 Design",
                    description = "Google's latest design system featuring accessible typography, dynamic colors, and elevation.",
                    icon = "✨"
                ),
                ShowcaseFeature(
                    id = "state",
                    title = "Reactive State & Flows",
                    description = "Kotlin Coroutines & StateFlow seamlessly deliver live updates to your Composable screens.",
                    icon = "⚡"
                ),
                ShowcaseFeature(
                    id = "arch",
                    title = "Clean Android Architecture",
                    description = "Separation of concerns using unidirectional data flow (UDF), ViewModel, and Repository.",
                    icon = "🏛️"
                )
            )
        )
    }
}
