package com.example.showcaseapp.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.example.showcaseapp.data.DefaultDataRepository
import com.example.showcaseapp.data.ShowcaseFeature
import com.example.showcaseapp.ui.theme.ShowcaseAppTheme
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    isDarkTheme: Boolean = false,
    onToggleDarkTheme: () -> Unit = {},
    onItemClick: (NavKey) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = viewModel { MainScreenViewModel(DefaultDataRepository()) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        MainScreenUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is MainScreenUiState.Success -> {
            MainContent(
                data = state.data,
                isDarkTheme = isDarkTheme,
                onToggleDarkTheme = onToggleDarkTheme,
                onIncrement = { viewModel.incrementCounter(it) },
                onDecrement = { viewModel.decrementCounter(it) },
                onReset = { viewModel.resetCounter() },
                onNameChange = { viewModel.onGreetingNameChange(it) },
                onToggleTag = { viewModel.toggleTag(it) },
                onToggleBookmark = { viewModel.toggleBookmark() },
                modifier = modifier
            )
        }
        is MainScreenUiState.Error -> {
            Box(
                modifier = modifier.fillMaxSize().padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error: ${state.throwable.message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun MainContent(
    data: MainScreenUiData,
    isDarkTheme: Boolean,
    onToggleDarkTheme: () -> Unit,
    onIncrement: (Int) -> Unit,
    onDecrement: (Int) -> Unit,
    onReset: () -> Unit,
    onNameChange: (String) -> Unit,
    onToggleTag: (String) -> Unit,
    onToggleBookmark: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "📱", fontSize = 22.sp)
                        Text(
                            text = "First Mobile App",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onToggleDarkTheme) {
                        Text(
                            text = if (isDarkTheme) "🌙" else "☀️",
                            fontSize = 20.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onIncrement(1)
                    scope.launch {
                        snackbarHostState.showSnackbar("Counter updated! Total: ${data.counter.count + 1}")
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Text(text = "⚡ +1", fontWeight = FontWeight.Bold)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // 1. Hero Card
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🚀", fontSize = 24.sp)
                            }
                            Column {
                                Text(
                                    text = "Hello, Android Developer!",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "Built with Kotlin & Jetpack Compose",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Current Theme:",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = if (isDarkTheme) "🌙 Dark Mode" else "☀️ Light Mode",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }

            // 2. Interactive Counter Card
            item {
                val count = data.counter.count
                val (badgeText, badgeColor) = when {
                    count >= 20 -> "🏆 Master Level" to MaterialTheme.colorScheme.tertiary
                    count >= 10 -> "🔥 Pro Builder" to MaterialTheme.colorScheme.primary
                    count >= 5 -> "⚡ Rising Star" to MaterialTheme.colorScheme.secondary
                    else -> "🌱 Getting Started" to MaterialTheme.colorScheme.outline
                }
                val progress = (count % 20) / 20f

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Interactive State Tracker",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = badgeColor.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = badgeText,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = badgeColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "$count",
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(
                            text = "Total interactions: ${data.counter.clicks}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onIncrement(1) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("+1")
                            }
                            FilledTonalButton(
                                onClick = { onIncrement(5) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("+5")
                            }
                            OutlinedButton(
                                onClick = { onDecrement(1) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("-1")
                            }
                            OutlinedButton(
                                onClick = onReset,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("↺")
                            }
                        }
                    }
                }
            }

            // 3. Reactive Playground Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Reactive Input Playground",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Type below to see Compose state update UI instantaneously:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = data.greetingName,
                            onValueChange = onNameChange,
                            label = { Text("Your name or motto") },
                            placeholder = { Text("e.g. Alex or Build Cool Apps") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                val displayName = data.greetingName.ifBlank { "Explorer" }
                                Text(
                                    text = "👋 Welcome, $displayName!",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Every keystroke triggers Compose recomposition cleanly without boilerplate.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Select Skills / Interests:",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            val availableTags = listOf("Compose", "Kotlin", "Material 3", "Coroutines", "Architecture")
                            availableTags.forEach { tag ->
                                val selected = tag in data.selectedTags
                                FilterChip(
                                    selected = selected,
                                    onClick = { onToggleTag(tag) },
                                    label = { Text(tag) }
                                )
                            }
                        }
                    }
                }
            }

            // 4. Bookmark & Satisfaction Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleBookmark() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (data.isBookmarked)
                            MaterialTheme.colorScheme.tertiaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = if (data.isBookmarked) "⭐" else "☆",
                                fontSize = 24.sp
                            )
                            Column {
                                Text(
                                    text = if (data.isBookmarked) "Favorited First App!" else "Add to Favorites",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "Tap to toggle favorite status",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                        Text(
                            text = if (data.isBookmarked) "Saved ✓" else "Tap",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // 5. Features Section Header
            item {
                Text(
                    text = "Core Android Concepts in this Project",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }

            // Feature Cards
            items(data.features, key = { it.id }) { feature ->
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = feature.icon, fontSize = 20.sp)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = feature.title,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = feature.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ShowcaseAppTheme {
        MainContent(
            data = MainScreenUiData(
                features = listOf(
                    ShowcaseFeature("1", "Jetpack Compose", "Modern declarative UI toolkit", "🎨"),
                    ShowcaseFeature("2", "Material 3", "Dynamic theming and adaptive layouts", "✨")
                ),
                counter = CounterState(count = 7, clicks = 12),
                greetingName = "Android Coder"
            ),
            isDarkTheme = false,
            onToggleDarkTheme = {},
            onIncrement = {},
            onDecrement = {},
            onReset = {},
            onNameChange = {},
            onToggleTag = {},
            onToggleBookmark = {}
        )
    }
}
