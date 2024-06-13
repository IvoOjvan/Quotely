package com.example.qoutely.view

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.qoutely.R
import com.example.qoutely.ui.theme.InterFontFamily
import com.example.qoutely.viewmodel.HomeViewModel


@Composable
fun HomeScreen(
    viewModel: HomeViewModel
){
    val channelId = "MyTestChannel1"
    val notificationId = 1
    val bigText = "Hello, we're glad " +
            "to see you back! Start exploring new quotes and " +
            "revisit your saved favorites. Let the inspiration begin!"
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        createNotificationChannel(channelId, context)
        showLargeTextNorification(
            context,
            channelId,
            notificationId,
            "Welcome to Quotely",
            bigText
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            HomeTopAppBar(viewModel = viewModel)
        }
    ) {values ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(values),
            verticalArrangement = Arrangement.Center,
            //horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderSection()
            Spacer(modifier = Modifier.height(16.dp))
            BodySection()
            Spacer(modifier = Modifier.height(40.dp))
            FeaturesSection()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(viewModel: HomeViewModel){
    TopAppBar(
        title = {
            Text(
                text = "Welcome ${viewModel.state.firstname} ${viewModel.state.lastname}",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
        },
        actions = {
            IconButton(onClick = {
                viewModel.logoutUser()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.logout),
                    contentDescription = "Icon search",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}

@Composable
private fun FeaturesSection(){
    Text(
        text = "Key Features:",
        fontSize = 24.sp,
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "search icon",
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Search Yor Quotes",
            fontSize = 20.sp,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium
        )
    }
    Spacer(modifier = Modifier.height(20.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "search icon",
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Save Your Favourites",
            fontSize = 20.sp,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium
        )
    }
    Spacer(modifier = Modifier.height(20.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            painterResource(id = R.drawable.icon_organize),
            contentDescription = "search icon",
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Organite Saved Quotes",
            fontSize = 20.sp,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun BodySection(){
    Text(
        text = "Discover, Save, and Organize Your Favourite Quotes!",
        fontSize = 20.sp,
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "Quotely is your ultimate companion for finding and collecting " +
                "the quotes that inspire you the most. Whether you're seeking " +
                "motivation, wisdom, or just a touch of humor, Quotely makes it " +
                "easy to search and save quotes that resonate with you.",
        fontSize = 16.sp,
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}

@Composable
private fun HeaderSection(){
    Text(
        text = "Welcome to Quotely",
        fontSize = 30.sp,
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}

fun createNotificationChannel(channelId: String, context: Context){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "MyTestChannel"
        val descriptionText = "Chanel for greeting user"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

@SuppressLint("MissingPermission")
fun showLargeTextNorification(
    context: Context,
    channelId: String,
    notificationId: Int,
    textTitle: String,
    textContent: String,
    priority: Int = NotificationCompat.PRIORITY_DEFAULT
){
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.notification_icon)
        .setContentTitle(textTitle)
        .setContentText(textContent)
        .setStyle(
            NotificationCompat.BigTextStyle().bigText(textContent)
        )
        .setPriority(priority)

    with(NotificationManagerCompat.from(context)){
        notify(notificationId, builder.build())
    }
}