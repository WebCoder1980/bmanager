plugins {
	java
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.bmanager"
version = "1.0.0-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.security:spring-security-web:6.3.4")
	implementation("org.springframework.security:spring-security-core:6.3.4")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.mockito:mockito-core:5.14.2")

	implementation("org.hibernate:hibernate-core:6.6.0.Final")
	implementation("org.apache.logging.log4j:log4j-core:2.20.0")
	implementation("org.apache.logging.log4j:log4j-api:2.20.0")
	implementation("org.postgresql:postgresql:42.7.4")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
