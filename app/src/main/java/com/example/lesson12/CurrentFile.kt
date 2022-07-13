package com.example.lesson12

class CurrentFile(private var name: String?, private var data: String?) {
    fun getName(): String? {
        return name
    }

    fun getData(): String? {
        return data
    }
}