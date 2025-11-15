plugins {
    java
    idea
    application
    id ("org.openjfx.javafxplugin") version "0.1.0"
}

group = "github.businessdirt"
version = "0.1.0"

repositories {
    mavenCentral()
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(23))

dependencies {
    compileOnly("org.jetbrains:annotations:26.0.2-1")
    testCompileOnly("org.jetbrains:annotations:26.0.2-1")

    // https://junit.org/
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // https://github.com/google/gson
    implementation("com.google.code.gson:gson:2.13.2")

    // https://logging.apache.org/log4j/2.x/manual/index.html
    implementation(platform("org.apache.logging.log4j:log4j-bom:2.25.2"))
    implementation("org.apache.logging.log4j:log4j-api:2.21.0")
    implementation("org.apache.logging.log4j:log4j-core:2.21.0")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.21.0")

    implementation("com.jfoenix:jfoenix:9.0.10")
    implementation("de.jensd:fontawesomefx-fontawesome:4.7.0-9.1.2")
    implementation("org.kordamp.bootstrapfx:bootstrapfx-core:0.4.0")
    implementation("fr.brouillard.oss:cssfx:11.5.0")
    implementation("org.controlsfx:controlsfx:11.2.1")
    implementation("com.github.almasb:fxgl:21.1")
}

javafx {
    version = "25"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.web")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("failed")
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Xlint:all")
}