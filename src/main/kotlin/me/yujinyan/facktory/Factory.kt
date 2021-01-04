package me.yujinyan.facktory

import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import java.util.*
import kotlin.internal.OnlyInputTypes
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

/**
 * Helper function to obtain a factory to generate instances of class [T]
 */
public inline fun <reified T : Any> factory(): Factory<T> = Factory(T::class)

/**
 * Factory that generates instances of [T].
 */
public class Factory<T : Any>(private val kClass: KClass<T>) {

    private val tempGeneratorValueMap = mutableMapOf<String, () -> Any>()

    private val easyRandomParameters = EasyRandomParameters()
        .randomize(Int::class.java) { (1..9999).random() }
        .dateRange(today(), today() + 2.months)
        .randomizerProvider(FixedGeneratorRandomizerProvider(tempGeneratorValueMap))

    private var factory = EasyRandom(easyRandomParameters)


    /**
     * Generate an instance of [T].
     */
    public fun make(): T {
        return factory.nextObject(kClass.java)!!
    }

    /**
     * Generated [count] instances of [T].
     */
    public fun make(count: Int): Iterable<T> {
        return mutableListOf<T>().apply {
            repeat(count) { add(make()) }
        }
    }

    /**
     * Generate sample data.
     *
     * Fields can be customized by calling methods on [Config] in the receiver.
     *
     * This method is **NOT** thread-safe.
     */
    public fun make(block: Config.() -> Unit): T {
        val scope = Config()
        scope.block()
        // TODO should clear map after invocation
        tempGeneratorValueMap.putAll(scope.generators)
        return make()
    }

    /**
     * Generated [count] instances of [T].
     *
     * Fields can be customized by calling methods on [Config] in the receiver.
     *
     * This method is **NOT** thread-safe.
     */
    public fun make(count: Int = 1, block: Config.() -> Unit): List<T> {
        val scope = Config()
        scope.block()
        // TODO should clear map after invocation
        tempGeneratorValueMap.putAll(scope.generators)
        return List(count) { make() }

    }


    /**
     * Handle to customize how [Factory] generates data.
     */
    public class Config {
        internal val generators = mutableMapOf<String, () -> Any>()

        /**
         * Supply a lambda to generate data for the property.
         */
        public infix fun <@OnlyInputTypes R> KProperty1<*, R>.by(block: () -> R) {
            @Suppress("UNCHECKED_CAST")
            this@Config.generators[this.name] = block as () -> Any
        }

        /**
         * Supply a lambda to generate data for nested fields represented by [KPropertyPath].
         *
         * [KPropertyPath] can be obtained by using [KPropertyPath.div].
         */
        public infix fun <R : Any?> KPropertyPath<R, *>.by(block: () -> R) {
            @Suppress("UNCHECKED_CAST")
            this@Config.generators[asString(this)] = block as () -> Any
        }
    }
}
