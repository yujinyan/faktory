package me.yujinyan.faktory

data class Post(
    val title: String,
    val body: String,
    val author: Author,
)

data class Author(
    val name: String
)


