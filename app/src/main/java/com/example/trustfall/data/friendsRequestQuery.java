package com.example.trustfall.data;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("friendsRequests")
public class friendsRequestQuery extends ParseObject {
    //Getters
    public ParseUser getRequester(){
        return (ParseUser) get("requester");
    }
    public ParseUser getRequested(){
        return (ParseUser) get("requested");
    }

    //Setters
    public void putRequester(ParseUser requester){
        put("requester", requester);
    }
    public void putRequested(ParseUser requested){
        put("requested", requested);
    }

}
