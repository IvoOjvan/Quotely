package com.example.qoutely.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.qoutely.viewmodel.SearchViewModel
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.font.FontWeight
import com.example.qoutely.components.ExpandableCard
import com.example.qoutely.ui.theme.InterFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel){
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            //MySearchTopAppBar(viewModel = viewModel)
            CustomCategoryDropdownMenu(viewModel = viewModel)
            
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { values ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(values)
        ) {
            items(
                items = viewModel.targetQuotes,
                itemContent = { item ->
                    ExpandableCard(
                        quote = item,
                        viewModel = viewModel,
                        snackbarHostState = snackbarHostState,
                        scope = scope
                    )
                })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomCategoryDropdownMenu(viewModel: SearchViewModel){
    var menuExpanded by remember { mutableStateOf(false) }
    var category : String by remember { mutableStateOf("Pick a cetegory") }
    val categories = listOf(
        "Motivation",
        "Life",
        "Wisdom",
        "Love",
        "Technology",
        "Creativity",
        "Courage",
        "Nature",
        "Change",
        "Inspiration",
        "Leadership",
        "Happiness",
        "Perseverance",
        "Imagination",
        "Hope",
        "Friendship"
    )

    val shape = if (menuExpanded) RoundedCornerShape(8.dp).copy(bottomEnd = CornerSize(0.dp), bottomStart = CornerSize(0.dp))
                else RoundedCornerShape(8.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        ExposedDropdownMenuBox(
            modifier = Modifier.padding(end = 20.dp),
            expanded = menuExpanded,
            onExpandedChange = {
                menuExpanded = !menuExpanded
            }
        ) {
            TextField(
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                value = category,
                onValueChange = {},
                label = {
                    Text(
                        text = "Category",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpanded) },
                shape = shape,
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Transparent,
                )
            )
            ExposedDropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                categories.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item,
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Normal
                            )
                        },
                        onClick = {
                            category = item
                            menuExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
        IconButton(
            modifier = Modifier.size(35.dp),
            onClick = {
            viewModel.getQuotesByCategory(category)
            }
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Icon search"
            )
        }
    }
}