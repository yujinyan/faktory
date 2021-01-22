package me.yujinyan.faktory

data class Post(
    val title: String,
    val body: String,
    val author: Author,
    val images: List<Image>
)

data class Author(
    val name: String
)

data class Image(
    val url: String
)


