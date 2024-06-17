package com.google.mapi

class Greeting {
    private val platform: Platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}

data class User(val name: String, val token: String)

data class Prompt(val text: String)

data class Place(val name: String, val url: String, val address: Address)

data class Address(val address: String, val countryCode: String)