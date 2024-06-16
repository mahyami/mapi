package com.google.mapi

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform