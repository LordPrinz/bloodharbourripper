plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // Publishing to Modrinth + CurseForge (https://modmuss50.github.io/mod-publish-plugin/)
    implementation("me.modmuss50.mod-publish-plugin:me.modmuss50.mod-publish-plugin.gradle.plugin:2.2.1")
}
