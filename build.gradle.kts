plugins {
    kotlin("jvm") version "1.4.21"
}

group = "me.yujinyan"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jeasy:easy-random-core:4.1.0")
    implementation("com.github.javafaker:javafaker:0.12")
}
