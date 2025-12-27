plugins {
    application
    java
    id("org.openjfx.javafxplugin") version "0.0.13"
}

group = "az.zakariyya"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(21)
}

javafx {
    version = "21.0.6"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    // run YOUR app
    mainClass.set("rpg.fx.FxMain")
}
