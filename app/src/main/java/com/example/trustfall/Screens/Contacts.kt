package com.example.trustfall.Screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trustfall.ui.theme.primary
import com.example.trustfall.ui.theme.secondary
import com.parse.Parse
import com.parse.ParseQuery
import com.parse.ParseUser

@Preview
@Composable
fun ContactScreen() {
    ContactDisplayScreen()
}

@Composable
fun ContactDisplayScreen() {
    val friendsList = query()
    val user = ParseUser.getCurrentUser()
    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(5.dp),
        border = BorderStroke(1.dp, Color.Black),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(primary)
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Contacts",
                fontFamily = fontfamily("Lobster Two"),
                fontSize = 32.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.padding(5.dp))
            friendsDisplayCard(user)
            /*if(friendsList.isEmpty()){
                Text("No emergency contact found")
            }else{
                friendsList.sortedBy {
                    it.username
                }
                friendsList.forEachIndexed { index, parseUser ->
                    friendsDisplayCard(parseUser)
                }
            }*/
        }
    }
}
@Composable
fun UserHead(
    firstName: String,
    lastName: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
) {
    Box(modifier.size(size), contentAlignment = Alignment.Center) {
        val initials = (firstName.take(1) + lastName.take(1)).uppercase()
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(SolidColor(secondary))
        }
        Text(text = initials, color = Color.White)
    }
}
@Composable
fun friendsDisplayCard(user: ParseUser) {

    Row(modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        UserHead(user.get("firstName").toString(), user.get("lastName").toString(), modifier = Modifier.padding(5.dp))
        Text(user.get("firstName").toString() + " " + user.get("lastName").toString(), fontSize = 25.sp, fontFamily = fontfamily("Oswald"))
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                val borderSize = 1.dp.toPx()
                val y = size.height - borderSize
                drawLine(
                    color = Color.Gray,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = borderSize
                )

            })
}

fun query(): List<ParseUser> {
    var currentUser = ParseUser.getCurrentUser()
    var resultList: ArrayList<ParseUser> = ArrayList()
    var currentList = currentUser.get("friendsList") as List<ParseUser>
    var userQuery = ParseUser.getQuery()
    userQuery.findInBackground { objects, e ->
        objects.forEachIndexed { index, parseUser ->
            if (currentList.contains(parseUser)) {
                resultList.add(parseUser)
            }
        }
    }
    if (resultList.size != currentList.size) {
        currentUser.put("friendsList", resultList)
        currentUser.saveInBackground { e ->
            if (e != null) {
                Log.e("Contacts.kt", "SaveException:" + e.toString())
            }
        }
    }

    return resultList
}