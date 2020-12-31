package me.yujinyan.facktory

import org.jeasy.random.DefaultRegistriesRandomizerProvider
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.annotation.Priority
import org.jeasy.random.api.Randomizer
import org.jeasy.random.api.RandomizerContext
import org.jeasy.random.api.RandomizerProvider
import org.jeasy.random.api.RandomizerRegistry
import java.lang.reflect.Field
import java.util.*
import kotlin.internal.OnlyInputTypes
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

@Priority(11)
class FixedGeneratorRandomizerProvider(
    private val map: Map<String, () -> Any>
) : RandomizerProvider {

    private val defaultProvider = DefaultRegistriesRandomizerProvider()
    private fun getKey(field: Field?, context: RandomizerContext?) =
        context?.currentField.let { if (it.isNullOrEmpty()) "" else "$it." } + field?.name

    override fun getRandomizerByField(field: Field?, context: RandomizerContext?): Randomizer<*>? {
        return map[getKey(field, context)]?.let { Randomizer { it() } }
            ?: defaultProvider.getRandomizerByField(field, context)
    }

    override fun <T : Any?> getRandomizerByType(type: Class<T>?, context: RandomizerContext?): Randomizer<T>? {
        return defaultProvider.getRandomizerByType(type, context)
    }

    override fun setRandomizerRegistries(randomizerRegistries: MutableSet<RandomizerRegistry>?) {
        defaultProvider.setRandomizerRegistries(randomizerRegistries)
    }
}

class Factory<T : Any>(private val kClass: KClass<T>) {
    private val faker = MyFaker(Locale.SIMPLIFIED_CHINESE)

    private val tempGeneratorValueMap = mutableMapOf<String, () -> Any>()

    private val easyRandomParameters = EasyRandomParameters()
        .randomize(Int::class.java) { (1..9999).random() }
        .dateRange(today(), today() + 2.months)
        .randomizerProvider(FixedGeneratorRandomizerProvider(tempGeneratorValueMap))

    private var factory = EasyRandom(easyRandomParameters)


    fun make(): T {
        return factory.nextObject(kClass.java)!!
    }

    fun make(count: Int): Iterable<T> {
        return mutableListOf<T>().apply {
            repeat(count) { add(make()) }
        }
    }

    class ConfigScope {
        val generators = mutableMapOf<String, () -> Any>()

        infix fun <@OnlyInputTypes R> KProperty1<*, R>.by(block: () -> R) {
            @Suppress("UNCHECKED_CAST")
            this@ConfigScope.generators[this.name] = block as () -> Any
        }

        infix fun <R : Any?> KPropertyPath<R, *>.by(block: () -> R) {
            @Suppress("UNCHECKED_CAST")
            this@ConfigScope.generators[asString(this)] = block as () -> Any
        }

        operator fun <T, U> KProperty<T?>.div(other: KProperty1<T, U?>) =
            KPropertyPath(this, other)
    }

    fun make(block: ConfigScope.() -> Unit): T {
        val scope = ConfigScope()
        scope.block()
        // TODO should clear map after invocation
        tempGeneratorValueMap.putAll(scope.generators)
        return make()
    }

    fun make(count: Int = 1, block: ConfigScope.() -> Unit): List<T> {
        val scope = ConfigScope()
        scope.block()
        // TODO should clear map after invocation
        tempGeneratorValueMap.putAll(scope.generators)
        return List(count) { make() }

    }
}

inline fun <reified T : Any> factory() = Factory(T::class)

class KPropertyPath<T, U>(
    internal val parent: KProperty<U?>,
    internal val child: KProperty1<U, T>
) : KProperty<T> by child

fun asString(property: KProperty<*>): String {
    return when (property) {
        is KPropertyPath<*, *> ->
            "${asString(property.parent)}.${property.child.name}"
        else -> property.name
    }
}