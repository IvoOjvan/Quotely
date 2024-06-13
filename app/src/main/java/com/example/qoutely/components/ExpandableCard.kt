package com.example.qoutely.components

import android.view.View
import androidx.annotation.RestrictTo
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ContentAlpha
import com.example.qoutely.R
import com.example.qoutely.network.model.Quote
import com.example.qoutely.ui.theme.Shapes
import com.example.qoutely.viewmodel.SavedViewModel
import com.example.qoutely.viewmodel.SearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Scope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableCard(
    quote: Quote,
    viewModel : ViewModel,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
){
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if(expandedState) 180f else 0f
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
            .padding(16.dp),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        border = BorderStroke(1.dp, Color.Black),
        onClick = {
            expandedState = !expandedState
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = quote.quote.toString(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = if(expandedState) 10 else 2,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    modifier = Modifier
                        .weight(6f)
                )
                IconButton(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium)
                        .weight(1f)
                        .rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                    }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown icon"
                    )
                }
            }
            if(expandedState){
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = quote.description.toString(),
                    overflow = TextOverflow.Ellipsis,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = quote.author.toString(),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )
                if(viewModel is SearchViewModel){
                    IconButton(
                        modifier = Modifier.align(Alignment.End),
                        onClick = {
                            //viewModel.saveQuote(quote)
                            viewModel.saveQuote(quote){
                                message ->
                                    scope.launch{
                                        snackbarHostState.showSnackbar(message)
                                    }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add icon",
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                }else if(viewModel is SavedViewModel){
                    IconButton(
                        modifier = Modifier.align(Alignment.End),
                        onClick = {
                            viewModel.removeQuote(quote){
                                message ->
                                scope.launch {
                                    snackbarHostState.showSnackbar(message)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Add icon",
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                }
            }
        }
    }
}

/*
@Composable
@Preview
fun ExpandableCardPreview(){
    ExpandableCard(Quote(), SearchViewModel())
}*/