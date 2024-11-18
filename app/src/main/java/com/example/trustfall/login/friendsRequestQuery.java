package com.example.trustfall.login;

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
    public Boolean getAccepted(){return (Boolean) get("accepted");}
    public String getObjectId(){return (String) get("objectId");}
    //Setters
    public void putRequester(ParseUser requester){
        put("requester", requester);
    }
    public void putRequested(ParseUser requested){
        put("requested", requested);
    }
    public void putAccepted(Boolean accepted) { put("accepted", accepted); }

}
