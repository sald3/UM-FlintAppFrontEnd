package com.example.um_flintapplication.apiRequests

import com.google.gson.JsonArray
import java.util.StringJoiner

/*
    This is what is used to map the responses from the API so that we can reference them
        Each response will give a list of JSON objects back
        Not every parameter may be important, hence why NewsItem only has 3 variables
 */

data class NewsItem(
    val id: Int,
    val title: String,
    val excerpt: String,
    val url: String,
    val image_url: String
)

data class EventItem(
    val id: Int,
    val title: String,
    val photo: String,
)

data class AnnouncementItem(
    var title: String,
    var description: String,
    var dateStart: String
)

data class AuthCallback(
    var status: String = "failure",
    var details: String
)

data class Buildings(
    var name: String
)

data class BuildingRooms(
    var name: String,
    var id: Int
){
    override fun toString(): String = name
}

data class RoomAvailable(
    var startTime: String,
    var endTime: String
)

data class Colleges(
    var id: Int,
    var name: String
){
    override fun toString(): String = name
}

data class AdvisorDegrees(
    var id: Int,
    var name: String
)

data class Advisors(
    var id: Int,
    var name: String,
    var email: String,
    var degrees: ArrayList<AdvisorDegrees>,
    var curl: String
)

data class GenericResponse(
    var status: String,
    var message: String
)

data class UserDetails(
    var id: String,
    var email: String,
    var firstname: String,
    var surname: String,
    var role: Int
)

data class Thread(
    var uuid: String,
    var users: List<UserDetails>
){
    override fun toString(): String {
        var str: StringJoiner = StringJoiner(",")

        users.forEach { user ->
            str.add(user.email)
        }

        return str.toString()
    }
}

data class MessageThread(
    var messageUuid: String,
    var threadUuid: String,
    var message: String,
    var sendDate: String,
    var sender: UserDetails
)

data class CreateThread(
    val threadCreated: Boolean,
    val threadUuid: String
)

data class AcademicEvents(
    val data: JsonArray
)