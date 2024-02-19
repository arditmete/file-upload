import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.2"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.22"
	kotlin("plugin.spring") version "1.9.22"
}

group = "com.logikcull"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly ("org.projectlombok:lombok:1.18.22")
	annotationProcessor ("org.projectlombok:lombok:1.18.22")
	implementation ("javax.validation:validation-api:2.0.1.Final")
	implementation("org.hibernate.validator:hibernate-validator:6.2.0.Final")
	implementation ("org.springdoc:springdoc-openapi-ui:1.6.4")
	implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
	implementation ("org.reactivestreams:reactive-streams:1.0.3")
	implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.5.2")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
