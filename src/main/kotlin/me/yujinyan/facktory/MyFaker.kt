package me.yujinyan.facktory

import com.github.javafaker.Faker
import java.util.*

public class IdGenerator(private var current: Int = 1) {
    public fun nextId(): Int = current++
}

public class MyFaker(locale: Locale) : Faker(locale) {
    private val idGenerator = IdGenerator()
    public fun nextId(): Int = idGenerator.nextId()

    public fun topic(): String = listOf(
        "宅家的得闲饮茶时间",
        "美食翻车也是艺术品",
        "周末在家做什么vol2",
        "那些隐藏菜单/餐厅",
        "现在也想去打卡的网红店").random()
}
