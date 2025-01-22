plugins {
    java
    id("org.springframework.boot") version "2.7.10"
    id("io.spring.dependency-management") version "1.1.0"
    id("com.diffplug.spotless") version "6.22.0"
}

group = "com.nullnumber1"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    /**
     * Spring boot starters
     */
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    /**
     * Database
     */
    //implementation("org.liquibase:liquibase-core")
    runtimeOnly("org.postgresql:postgresql")

    /**
     * Utils & Logging
     */
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.2")
    implementation("org.projectlombok:lombok:1.18.26")
    implementation("org.jetbrains:annotations:24.0.1")
    annotationProcessor("org.projectlombok:lombok:1.18.26")

    /**
     * Tests
     */
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    /**
     * Pdf generation
     */
    implementation("com.itextpdf:itextpdf:5.5.13.3")

    /**
     * Documentation
     */
    implementation("org.springdoc:springdoc-openapi-ui:1.7.0")

    /**
     * Transactions
     */
    implementation("org.springframework.boot:spring-boot-starter-jta-bitronix:2.4.13")

    implementation("org.springframework.boot:spring-boot-starter-mail:2.5.6")
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    implementation("io.projectreactor:reactor-core:3.4.14")
    testImplementation("io.projectreactor:reactor-test")
    implementation("net.javacrumbs.shedlock:shedlock-spring:4.42.0") // Замените на актуальную версию
    implementation("net.javacrumbs.shedlock:shedlock-provider-jdbc-template:4.42.0") // Замените на актуальную версию
    implementation ("org.flywaydb:flyway-core")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val test by tasks.getting(Test::class) { testLogging.showStandardStreams = true }

spotless {
    java {
        target("**/src/**/*.java")
        removeUnusedImports()
        googleJavaFormat("1.15.0")
    }
}