package com.vdggrtf.playlog

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform