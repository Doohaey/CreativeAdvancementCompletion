pluginManagement {
    val kotlinVersion: String by settings
    val shadowVersion: String by settings
    val runPaperVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        id("com.gradleup.shadow") version shadowVersion
        id("xyz.jpenilla.run-paper") version runPaperVersion
    }

    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

rootProject.name = "CreativeAdvancementCompletion"