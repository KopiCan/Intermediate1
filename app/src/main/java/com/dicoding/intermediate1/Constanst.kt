package com.dicoding.intermediate1

object Constanst {

    enum class DetailStory{
        UserName, ImageURL, Description
    }

    enum class ProfilePreferences {
        ProfileID, ProfileName, ProfleEmail, ProfileToken
    }

    val emailDefaultPattern = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    const val preferencesName = "Settings"
    const val defaultValue = "Not Set"

}
