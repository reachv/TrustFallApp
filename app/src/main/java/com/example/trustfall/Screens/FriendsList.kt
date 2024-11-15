package com.example.trustfall.Screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trustfall.data.friendsRequestQuery
import com.example.trustfall.ui.theme.primary
import com.example.trustfall.ui.theme.secondary
import com.parse.ParseQuery
import com.parse.ParseUser

@Composable
fun friendsListView() {
    friendsListDisplayScreen()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun friendsListDisplayScreen() {
    //Variables and Values
    val friendsList = friendsListQuery()
    val friendsRequestList = requestQuery()
    val user = ParseUser.getCurrentUser()

    //Composable
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
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
                .padding(15.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                BadgedBox(
                    badge = {
                        if(friendsRequestList.size > 0){
                            Badge(Modifier.offset(y=12.dp,x=-5.dp)){Text("${friendsRequestList.size}")}
                        }
                    },
                    modifier = Modifier.wrapContentSize()
                ) {
                    Icon(imageVector = Icons.Default.Mail, "Inbox", modifier = Modifier.padding(top = 5.dp))
                }
                Text(
                    text = "Friends List",
                    fontFamily = fontfamily("Lobster Two"),
                    fontSize = 32.sp
                )
                Icon(imageVector = Icons.Default.Add, "Add Friends", modifier = Modifier.padding(top = 5.dp))
            }
            Spacer(modifier = Modifier.padding(5.dp))
            if(friendsList.isEmpty()){
                Text("No emergency contact found", modifier = Modifier.align(Alignment.CenterHorizontally), fontFamily = fontfamily("Oswald"))
                Text("Add friends using the plus sign", modifier = Modifier.align(Alignment.CenterHorizontally), fontFamily = fontfamily("Oswald"))
            }else{
                friendsList.sortedBy {
                    it.username
                }
                friendsList.forEachIndexed { index, parseUser ->
                    friendsDisplayCard(parseUser)
                    if(!(friendsList.size - 1 == index)){
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
                                }
                        )
                    }
                }
            }
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
}

fun friendsListQuery(): List<ParseUser>{
    var currentUser = ParseUser.getCurrentUser()
    var friendsList: ArrayList<ParseUser> = ArrayList()
    var currentList = currentUser.get("friendsList") as List<ParseUser>
    var userQuery = ParseUser.getQuery()
    userQuery.findInBackground { objects, e ->
        currentList.forEachIndexed { index, parseUser ->
            if(objects.contains(parseUser)){
                friendsList.add(parseUser)
            }
        }
    }
    if (friendsList.size != currentList.size) {
        currentUser.put("friendsList", friendsList)
        currentUser.saveInBackground { e ->
            if (e != null) {
                Log.e("FriendsList.kt", "SaveException:" + e.toString())
            }
        }
    }
    return friendsList
}
fun requestQuery(): List<friendsRequestQuery>{
    var friendsRequest= ArrayList<friendsRequestQuery>()
    var friendsQuery: ParseQuery<friendsRequestQuery> = ParseQuery("friendsRequests")
    friendsQuery.whereEqualTo("requester", ParseUser.getCurrentUser())
    friendsQuery.findInBackground { objects, e ->
        if(e != null){
            Log.e("FriendsRequest", "FriendsRequestException" + e)
            return@findInBackground
        }
        objects.forEachIndexed { index, FRQ ->
            if(FRQ.requested != null){
                friendsRequest.add(FRQ)
            }else{
                FRQ.deleteInBackground()
            }
        }
    }
    return friendsRequest
}

