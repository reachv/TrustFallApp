package com.example.trustfall.screens

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trustfall.login.friendsRequestQuery
import com.example.trustfall.ui.theme.primary
import com.example.trustfall.ui.theme.secondary
import com.parse.ParseQuery
import com.parse.ParseUser
import java.util.regex.Pattern

@Composable
fun friendsListView() {
    var navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "friendsListDisplay"
    ) {
        composable("friendsListDisplay") {
            friendsListDisplayScreen(navController)
        }
        composable("inboxDisplay") {
            inboxDisplay(navController)
        }
        composable("addfriendsDisplay") {
            addfriendsDisplay(navController)
        }
    }
}

@Composable
fun addfriendsDisplay(navController: NavHostController) {
    var userCode by remember { mutableStateOf("") }
    var context = LocalContext.current as Activity
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primary)
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(primary),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            elevation = CardDefaults.cardElevation(10.dp),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Text(
                "Add friends",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontFamily = fontfamily("Lobster Two"),
                fontSize = 32.sp
            )
            OutlinedTextField(
                value = userCode,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp, top = 8.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(Color.White),
                onValueChange = { newText ->
                    userCode = newText
                },
                placeholder = {
                    Text(
                        "Enter user code",
                        color = Color.Gray,
                        fontFamily = fontfamily("Oswald")
                    )
                },
                textStyle = TextStyle(fontFamily = fontfamily("Oswald"))
            )
            Spacer(Modifier.padding(5.dp))
            Button(
                onClick = {
                    val codePattern: Pattern = Pattern.compile("[A-Za-z0-9]{9,11}")
                    if (codePattern.matcher(userCode)
                            .matches() && !userCode.equals(ParseUser.getCurrentUser().objectId)
                    ) {
                        var userQuery = ParseUser.getQuery()
                        userQuery.whereEqualTo("objectId", userCode)
                        userQuery.findInBackground { objects, e ->
                            objects.forEachIndexed { index, parseUser ->
                                var friendRequest = friendsRequestQuery()
                                friendRequest.putRequester(ParseUser.getCurrentUser())
                                friendRequest.putRequested(parseUser)
                                friendRequest.putAccepted(false)
                                friendRequest.saveInBackground {
                                    if (it != null) {
                                        Toast.makeText(
                                            context,
                                            "Unable to create request, please try again later.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        Log.e("FriendsList.kt", "FriendsAddRequest: " + it)
                                        return@saveInBackground
                                    }
                                    Toast.makeText(
                                        context,
                                        "Request successfully sent",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.navigate("friendsListDisplay")
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "User code does not meet requirement, please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            {
                Icon(imageVector = Icons.Default.Add, "Add Button")
            }
        }
    }
}

@Composable
fun inboxDisplay(navController: NavHostController) {
    val query = inboxQuery()
    var requestList = query["requested"]
    Log.e("return", "" + inboxQuery().size)
    val context = LocalContext.current as Activity
    Log.e("Asdf", "" + requestList?.size)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primary)
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Card(
            colors = CardDefaults.cardColors(primary),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            elevation = CardDefaults.cardElevation(10.dp),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Text(
                text = "Inbox",
                fontFamily = fontfamily("Lobster Two"),
                fontSize = 32.sp,
                modifier = Modifier
                    .padding(5.dp, top = 0.dp)
                    .align(Alignment.CenterHorizontally)
            )


            requestList?.forEachIndexed { index, friendsRequestQuery ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val requester = friendsRequestQuery.requester
                    friendsDisplayCard(requester.fetch())
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.weight(1f))
                    Text("Accept",
                        fontSize = 20.sp,
                        fontFamily = fontfamily("Oswald"),
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .clickable {
                                var currentUser = ParseUser.getCurrentUser()
                                var currentUserFriendList: ArrayList<ParseUser>
                                currentUserFriendList =
                                    currentUser.get("friendsList") as ArrayList<ParseUser>
                                currentUserFriendList.add(friendsRequestQuery.requester)
                                currentUser.put("friendsList", currentUserFriendList)
                                currentUser.saveInBackground {
                                    if (it != null) {
                                        Log.e("FriendsList", "" + it)
                                        return@saveInBackground
                                    }
                                    navController.navigate("friendsListDisplay")
                                }
                                friendsRequestQuery.putAccepted(true)
                                friendsRequestQuery.saveInBackground {
                                    Toast
                                        .makeText(context, "Successful", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                    )
                    Text("Decline",
                        fontSize = 20.sp,
                        fontFamily = fontfamily("Oswald"),
                        modifier = Modifier
                            .padding(end = 15.dp)
                            .clickable {
                                friendsRequestQuery.deleteInBackground()
                            })
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun friendsListDisplayScreen(navController: NavHostController) {
    //Variables and Values
    var query = inboxQuery()
    var friendsList: MutableList<ParseUser>
    friendsList = ParseUser.getCurrentUser().get("friendsList") as MutableList<ParseUser>
    var friendsRequestList: ArrayList<friendsRequestQuery> = query["accepted"]!!
    if (query["accepted"]?.size!! > 0) {
        query["accepted"]?.forEachIndexed { index, friendsRequestQuery ->
            if (friendsRequestQuery.accepted) {
                friendsList.add(friendsRequestQuery.requested)
                friendsRequestQuery.delete()
            }
        }
        var user = ParseUser.getCurrentUser()
        user.put("friendsList", friendsList)
        user.save()
    }


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
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                BadgedBox(
                    badge = {
                        if (friendsRequestList.size > 0) {
                            var count = 0
                            friendsRequestList.forEachIndexed { index, friendsRequestQuery ->
                                if(!friendsRequestQuery.accepted){
                                    count += 1
                                }
                            }
                            Badge(
                                Modifier.offset(
                                    y = 12.dp,
                                    x = -5.dp
                                )
                            ) { Text("$count") }
                        }
                    },
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable {
                            navController.navigate("inboxDisplay")
                        }
                ) {
                    Icon(
                        imageVector = Icons.Default.Mail,
                        "Inbox",
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }
                Text(
                    text = "Friends List",
                    fontFamily = fontfamily("Lobster Two"),
                    fontSize = 32.sp
                )
                Icon(
                    imageVector = Icons.Default.Add,
                    "Add Friends",
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .clickable {
                            navController.navigate("addfriendsDisplay")
                        }
                )
            }
            Spacer(modifier = Modifier.padding(5.dp))
            if (friendsList.isEmpty() == true) {
                Text(
                    "No emergency contact found",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontFamily = fontfamily("Oswald")
                )
                Text(
                    "Add friends using the plus sign",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontFamily = fontfamily("Oswald")
                )
            } else {
                friendsList?.sortedBy {
                    it.fetchIfNeeded().username
                }
                Log.e("here", "here1")
                friendsList.forEachIndexed { index, parseUser ->
                    friendsDisplayCard(parseUser)
                    if (friendsList.size - 1 != index) {
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
    size: Dp = 40.dp
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
    Row(
        modifier = Modifier.padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        UserHead(
            user.fetchIfNeeded().get("firstName").toString(),
            user.fetchIfNeeded().get("lastName").toString(),
            modifier = Modifier.padding(5.dp)
        )
        Text(
            user.fetchIfNeeded().get("firstName").toString() + " " + user.fetchIfNeeded().get("lastName").toString(),
            fontSize = 20.sp,
            fontFamily = fontfamily("Oswald")
        )
    }
}

fun inboxQuery(): HashMap<String, ArrayList<friendsRequestQuery>> {
    var result: HashMap<String, ArrayList<friendsRequestQuery>> = HashMap()
    result.put("accepted", ArrayList())
    result.put("requested", ArrayList())
    var request: ArrayList<friendsRequestQuery>

    var accepted = ParseQuery.getQuery<friendsRequestQuery>("friendsRequests")
    accepted.whereEqualTo("requester", ParseUser.getCurrentUser())

    var requested = ParseQuery.getQuery<friendsRequestQuery>("friendsRequests")
    accepted.whereEqualTo("requested", ParseUser.getCurrentUser())

    var friendsQuery: ParseQuery<friendsRequestQuery> = ParseQuery.or(listOf(accepted, requested))
    request = friendsQuery.find() as ArrayList<friendsRequestQuery>
    Log.e("requests", request.toString())

    request.forEachIndexed { index, friendsRequestQuery ->
        if (friendsRequestQuery.requester.fetch().objectId.toString() == ParseUser.getCurrentUser().objectId.toString()) {
            result["accepted"]?.add(friendsRequestQuery)
        } else {
            result["requested"]?.add(friendsRequestQuery)
        }
    }
/*    Log.e("here", result["requested"].toString())*/
    return result
}

