package me.yujinyan.faktory

import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

/**
 * Adapted from `org.springframework.data.mongodb.core.query.KPropertyPath`
 */

/**
 * Abstraction of a property path consisting of [KProperty]
 */
public class KPropertyPath<T, U>(
    internal val parent: KProperty<U?>,
    internal val child: KProperty1<U, T>
) : KProperty<T> by child

/**
 * Recursively construct field name for a nested property
 */
public fun asString(property: KProperty<*>): String {
    return when (property) {
        is KPropertyPath<*, *> ->
            "${asString(property.parent)}.${property.child.name}"
        else -> property.name
    }
}

/**
 * Builds [KPropertyPath] from [KProperty] references.
 * Refer to a field in a nested data bean.
 *
 * For example, referring to the field "post.author.name":
 * ```
 * Post::author / Author::name
 * ```
 */
public operator fun <T, U> KProperty<T?>.div(other: KProperty1<T, U?>): KPropertyPath<U?, T> =
    KPropertyPath(this, other)
