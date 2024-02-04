package com.example.realstudy

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

    fun getFeedForFriend() {
        TODO()
    }

    // Friend Network
}