plugins {
    id("java")
    id("org.openjfx.javafxplugin") version "0.0.13"
}

group = "mpp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("org.apache.logging.log4j:log4j-api:2.17.1")
    implementation("org.apache.logging.log4j:log4j-core:2.17.1")
    runtimeOnly("org.xerial:sqlite-jdbc:3.41.2.2")
    implementation("commons-codec:commons-codec:1.15")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.openjfx:javafx-controls:20.0.0.1")
    implementation("org.openjfx:javafx-fxml:20.0.0.1")
    implementation("org.hibernate.orm:hibernate-core:6.6.7.Final")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
}

javafx {
    version = "21.0.5"
    modules = listOf("javafx.controls", "javafx.fxml")
}

tasks.test {
    useJUnitPlatform()
}