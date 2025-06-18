plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.5"
    id("java")
}

group = "mpp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":OficiuModel"))
    implementation(project(":OficiuPersistence"))
    implementation(project(":OficiuServices"))

    implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.1")


    implementation("org.xerial:sqlite-jdbc:3.43.0.0")
    implementation("org.hibernate.orm:hibernate-community-dialects:6.3.1.Final")

    implementation("org.springframework.boot:spring-boot-starter-websocket")

    testImplementation("com.jayway.jsonpath:json-path")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}