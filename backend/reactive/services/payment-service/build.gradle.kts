plugins {
	id("java")
	id("org.springframework.boot") version "4.0.0"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.owasp.dependencycheck") version "12.1.9"
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-security")

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	implementation("org.postgresql:postgresql:42.7.8")

	compileOnly("org.projectlombok:lombok:1.18.38")
	annotationProcessor("org.projectlombok:lombok:1.18.38")

	implementation("org.mapstruct:mapstruct:1.6.3")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	implementation("commons-codec:commons-codec:1.19.0")
	implementation("com.nimbusds:nimbus-jose-jwt:10.6")
	implementation("com.github.ben-manes.caffeine:caffeine:3.2.3")
	implementation("org.bouncycastle:bcpkix-jdk18on:1.82")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
	val profile = project.findProperty("profile")?.toString() ?: "dev"

	sourceResources(sourceSets["main"])
	systemProperty("spring.profiles.active", profile)
}