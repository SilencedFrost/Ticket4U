plugins {
    id("java")
    id("org.springframework.boot") version "4.0.0" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("org.owasp.dependencycheck") version "12.1.9" apply false
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.owasp.dependencycheck")

    group = "org.example"
    version = "1.0-SNAPSHOT"

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }
    
    repositories {
        mavenCentral()
    }

    dependencies {
        // Spring boot
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        implementation("org.springframework.boot:spring-boot-starter-security")

        // Spring dev tools
        "developmentOnly"("org.springframework.boot:spring-boot-devtools")

        // Postgres driver
        implementation("org.postgresql:postgresql:42.7.8")

        // Lombok - compileOnly AND annotationProcessor
        compileOnly("org.projectlombok:lombok:1.18.38")
        annotationProcessor("org.projectlombok:lombok:1.18.38")

        // MapStruct
        implementation("org.mapstruct:mapstruct:1.6.3")
        annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

        // Binding for MapStruct and Lombok
        annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

        // Spring configuration processor - can be last
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

        // Testing
        testImplementation("org.springframework.boot:spring-boot-starter-test")

        // Commons codec (for SHA256)
        implementation("commons-codec:commons-codec:1.19.0")

        // Nimbus JOSE - JWT
        implementation("com.nimbusds:nimbus-jose-jwt:10.6")

        // Caffeine cache
        implementation("com.github.ben-manes.caffeine:caffeine:3.2.3")

        // Bouncy Castle Java APIs, for pem key parsing
        implementation("org.bouncycastle:bcpkix-jdk18on:1.82")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    // Enable annotation processing
    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-parameters")
    }

    tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
        val profile = project.findProperty("profile")?.toString() ?: "dev"

        sourceResources(sourceSets["main"])
        systemProperty("spring.profiles.active", profile)
    }

    tasks.register("runUser") {
        dependsOn(":services:user-service:bootRun")
    }

    tasks.register("runEvent") {
        dependsOn(":services:event-service:bootRun")
    }
}