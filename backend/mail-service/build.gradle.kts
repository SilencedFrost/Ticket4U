plugins {
	java
	id("org.springframework.boot") version "4.0.2"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.ticket4u"
version = "0.0.1-SNAPSHOT"
description = "Mail service for Ticket4U"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Spring Boot Core
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	
	// Template Engine cho email templates
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	
	// Brevo (Sendinblue) SDK
	implementation("com.sendinblue:sib-api-v3-sdk:6.0.0")
	
	// API Documentation - Swagger/OpenAPI (compatible with Spring Boot 4.x)
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.4")
	
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
