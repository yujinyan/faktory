package kotlin.internal

/**
 * https://youtrack.jetbrains.com/issue/KT-13198
 */
@Target(AnnotationTarget.TYPE_PARAMETER)
@Retention(AnnotationRetention.BINARY)
internal annotation class OnlyInputTypes