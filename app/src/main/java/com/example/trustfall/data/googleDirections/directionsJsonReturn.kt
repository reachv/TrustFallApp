package com.example.trustfall.data.googleDirections

data class directionsJsonReturn(
    val geocoded_waypoints: List<Geocoded_Waypoint>,
    val routes: List<Route>,
    val status: String
)

data class Geocoded_Waypoint (
    var geocoder_status : String,
    var place_id : String
)

data class Route (
    val summary: String,
    val overview_polyline: OverviewPolylineDto,
)

data class OverviewPolylineDto(
    val points: String
)

