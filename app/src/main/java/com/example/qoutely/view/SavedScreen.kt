package com.example.qoutely.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ChipBorder
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import com.example.qoutely.components.ExpandableCard
import com.example.qoutely.network.model.Quote
import com.example.qoutely.ui.theme.InterFontFamily
import com.example.qoutely.viewmodel.SavedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(viewModel: SavedViewModel){
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            MyTopAppBar(viewModel)
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
                        //
                        snackbarHostState = snackbarHostState,
                        scope = scope
                    )
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MyTopAppBar(viewModel: SavedViewModel){
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = "Favourite quotes",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
        },
        actions = {
            IconButton(onClick = { menuExpanded = !menuExpanded }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options"
                )
            }
            DropdownMenu(
                modifier = Modifier.width(290.dp),
                expanded = menuExpanded,
                onDismissRequest = {
                   menuExpanded = false
                }
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        "Categories",
                        //style = MaterialTheme.typography.titleSmall
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Medium,
                    )
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            //.padding(vertical = 2.dp),
                        ,maxItemsInEachRow = 3
                    ) {
                        viewModel.categories.forEach { category->
                            FilterChip(
                                modifier = Modifier.padding(6.dp),
                                onClick = {
                                    viewModel.updateCategories(category)

                                },
                                label = {
                                    Text(
                                        text = category,
                                        fontFamily = InterFontFamily,
                                        fontWeight = FontWeight.Normal,
                                    )
                                },
                                selected = if(viewModel.selectedCategories.contains(category)) true else false,
                                leadingIcon = if (viewModel.selectedCategories.contains(category)) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Done,
                                            contentDescription = "Done icon",
                                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                                        )
                                    }
                                } else {
                                    null
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Authors",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Medium,
                    )
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        maxItemsInEachRow = 3
                    ){
                        viewModel.authors.forEach { author ->
                            FilterChip(
                                modifier = Modifier.padding(6.dp),
                                onClick = {
                                    viewModel.updateAuthors(author)
                                },
                                label = {
                                    Text(
                                        text = author,
                                        fontFamily = InterFontFamily,
                                        fontWeight = FontWeight.Normal,
                                    )
                                },
                                selected = if(viewModel.selectedAuthors.contains(author)) true else false,
                                leadingIcon = if (viewModel.selectedAuthors.contains(author)) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Done,
                                            contentDescription = "Done icon",
                                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                                        )
                                    }
                                } else {
                                    null
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}