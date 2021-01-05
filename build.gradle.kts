plugins {
    kotlin("jvm") version "1.4.21"
    `maven-publish`
}

group = "me.yujinyan"
version = "0.0.1"

kotlin {
    explicitApi()

}

repositories {
    mavenCentral()
}


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xallow-kotlin-package")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jeasy:easy-random-core:4.1.0")
    implementation("com.github.javafaker:javafaker:0.12")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testImplementation("io.kotest:kotest-assertions-core:4.3.0") // for kotest core jvm assertions
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}
