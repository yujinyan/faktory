package me.yujinyan.faktory

import org.jeasy.random.DefaultRegistriesRandomizerProvider
import org.jeasy.random.annotation.Priority
import org.jeasy.random.api.Randomizer
import org.jeasy.random.api.RandomizerContext
import org.jeasy.random.api.RandomizerProvider
import org.jeasy.random.api.RandomizerRegistry
import java.lang.reflect.Field

@Priority(11)
internal class FixedGeneratorRandomizerProvider(
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