package com.yoavs.flickerbrowser

interface TagProvider {


    fun tag(): String {
        val className = this.javaClass.simpleName
        return if(className.length > 22) className.substring(0,22) else className
    }
}