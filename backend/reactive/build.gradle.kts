plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    group = "org.example"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    // Enable annotation processing
    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-parameters")
    }
}

tasks.register("runTicket") {
    dependsOn(":services:ticket-service:bootRun")
}

tasks.register("runPayment") {
    dependsOn(":services:payment-service:bootRun")
}