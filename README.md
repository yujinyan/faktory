# Faktory

*Faktory* implements [Object Mothers](https://martinfowler.com/bliki/ObjectMother.html) for Kotlin.

When we write tests or demos, it is often necessary to create some example data. Some of those "data beans" are composed of many other objects. Constructing such object graphs by hand is very tedious. 

This library helps you write those data bean generators in simple, idiomatic Kotlin code.

## Usage

Given following domain entity `Post`:
```kotlin
data class Post(
    val title: String,
    val body: String,
    val author: Author,
)

data class Author(val name: String)
```
You can write a custom data factory that generates test data:

```kotlin
val postFactory = factory<Post>()

val post: Post = postFactory.make()
val posts: Iterable<Post> = postFactory.make(3)
```

You can also customize on per-field basis:

```kotlin
val post = postFactory.make {
    Post::title by { "Hello World!" }
}

post.title // "Hello World!"
```