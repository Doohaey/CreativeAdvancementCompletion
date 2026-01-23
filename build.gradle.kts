plugins {
    kotlin("jvm")
    id("com.gradleup.shadow")
    id("xyz.jpenilla.run-paper")
}

val paperVersion: String by project
val javaVersion: String by project
val pluginVersion: String by project
val groupId: String by project
val pluginName: String by project

val apiVersion = paperVersion.split('.').take(2).joinToString(".")

group = groupId
version = pluginVersion

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("io.papermc.paper:paper-api:$paperVersion-R0.1-SNAPSHOT")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion.toInt()))
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.valueOf("JVM_${javaVersion}"))
    }
}

tasks.runServer {
    minecraftVersion(paperVersion)
}

tasks.processResources {
    val props = mapOf(
        "pluginName" to pluginName,
        "pluginVersion" to pluginVersion,
        "paperVersion" to apiVersion
    )
    inputs.properties(props)

    filesMatching("plugin.yml.template") {
        expand(props)
        name = "plugin.yml"
    }
}

tasks.named("shadowJar") {
    setProperty("archiveClassifier", "")
    val relocateMethod = this.javaClass.getMethod("relocate", String::class.java, String::class.java)
    relocateMethod.invoke(this, "kotlin", "org.imyvm.cac.libs.kotlin")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}