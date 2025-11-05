plugins {
    java
    idea
    application
}

group = "github.businessdirt"
version = "0.1.0"

repositories {
    mavenCentral()
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(23))

dependencies {
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
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("failed")
    }
}

tasks.test {
    useJUnitPlatform()
}