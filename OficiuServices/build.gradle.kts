plugins {
    id("java")
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
    implementation(project(":OficiuModel"))
    implementation(project(":OficiuPersistence"))
}

tasks.test {
    useJUnitPlatform()
}