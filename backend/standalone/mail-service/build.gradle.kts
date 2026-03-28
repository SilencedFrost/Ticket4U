import org.gradle.kotlin.dsl.named
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
	java
	id("org.springframework.boot") version "4.0.2"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.ticket4u"
version = "0.0.1-SNAPSHOT"
description = "Mail service for Ticket4U"

repositories {
	mavenCentral()
}

dependencies {
	// Spring Boot Core
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	
	// Template Engine cho email templates
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	
	// API Documentation - Swagger/OpenAPI (compatible with Spring Boot 4.x)
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.4")
	
	// Rate Limiting
	implementation("com.bucket4j:bucket4j-core:8.10.1")
	
	// Utilities
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	
	// JSON Processing
	implementation("com.fasterxml.jackson.core:jackson-databind")
	
	// Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testCompileOnly("org.projectlombok:lombok")
	testAnnotationProcessor("org.projectlombok:lombok")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
	val profile = project.findProperty("profile")?.toString() ?: "dev"

	sourceResources(sourceSets["main"])
	systemProperty("spring.profiles.active", profile)
}

tasks.named<BootBuildImage>("bootBuildImage") {
	imageName.set("${project.name}:latest")
}

tasks.register("runMail") {
	dependsOn("bootRun")
}

tasks.register("buildMail") {
	dependsOn("bootBuildImage")
}

