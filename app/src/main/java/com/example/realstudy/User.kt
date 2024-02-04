package com.example.realstudy

import android.se.omapi.Session
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference


class Profile(var displayName: String, var profilePicture: String)

class User(
    val userID: String,
    val friends: MutableList<String>,
    val profile: Profile,
    val sessions: MutableList<StudySession>,
) {
    fun addFriend(database: DatabaseReference, friend: User) {
        friends.add(friend.userID)
        database.child(userID).setValue(User(userID, friends, profile, sessions))
    }
    // adds friend by adding the passed-through friend onto users list of friends.

    fun removeFriend(database: DatabaseReference, friend: User) {
        friends.remove(friend.userID)
        database.child(userID).setValue(User(userID, friends, profile, sessions))
    }
    // removes friend by removing the passed-through friend from the users list of friends.
    // the friend removed should always be inside the users lists of friends

    fun updateName(database: DatabaseReference, newName: String) {
        val newProfile = Profile(newName, profile.profilePicture)
        database.child(userID).setValue(User(userID, friends, newProfile, sessions))
    }
    // sets a new profile with same pfp but new name

    fun updatePFP(database: DatabaseReference, pfpLink: String) {
        val newProfile = Profile(profile.displayName, pfpLink)
        database.child(userID).setValue(User(userID, friends, newProfile, sessions))
    }
    // sets a new profile with same name but new pfp

    // Firebase
    fun addSession(database: DatabaseReference, session: StudySession) {
        sessions.add(session)
        database.child(userID).setValue(User(userID, friends, profile, sessions))
    }

    fun removeSession(database: DatabaseReference, session: StudySession) {
        sessions.remove(session)
        database.child(userID).setValue(User(userID, friends, profile, sessions))
    }

    fun clearSessions(database: DatabaseReference) {
        database.child(userID).setValue(User(userID, friends, profile, mutableListOf()))
    }

    fun addToStorage() {
        TODO()
    }

    fun getFriends(snapshot: DataSnapshot): List<String> {
        return snapshot.child("friends").children.map { it.value as String }
    }

    fun getFeed(snapshot: DataSnapshot): List<Triple<String, String, List<StudySession>>> {
        // Get sessions (packaged per user with their "display data")
        val friendIDs = getFriends(snapshot.child(userID))
        return friendIDs.map { Triple(
            snapshot.child(it).child("profile").child("displayName").value as String,
            snapshot.child(it).child("profile").child("profilePicture").value as String,
            snapshot.child(it).child("sessions").children
                .map { c -> StudySession(
                    c.child("startTime").value as String,
                    c.child("endTime").value as String,
                    c.child("duration").value as String,
                    c.child("images").children
                        .map { img -> img.value as String }
                )  }
        ) }
    }

    fun getProfile(snapshot: DataSnapshot): Profile {
        return Profile(
            snapshot.child("profile").child("displayName").value as String,
            snapshot.child("profile").child("profilePicture").value as String
        )
    }

    fun getSessions(snapshot: DataSnapshot): MutableList<StudySession> {
        return snapshot.child("sessions").children
            .map { c -> StudySession(
                c.child("startTime").value as String,
                c.child("endTime").value as String,
                c.child("duration").value as String,
                c.child("images").children
                    .map { img -> img.value as String }
            )  }.toMutableList()
    }
}