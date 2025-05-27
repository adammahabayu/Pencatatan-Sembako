package com.example.pencatatansembako

sealed class SembakoItem {
    data class Header(val title: String) : SembakoItem()
    data class Content(val sembako: Sembako) : SembakoItem()
}