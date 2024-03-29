import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.1"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.22"
	kotlin("plugin.spring") version "1.9.22"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}


dependencies {
	/***************************** Kotlin Reflect *****************************************************/
	/* Runtime libraries for Kotlin reflection, which allows you to introspect the structure of your program at runtime. */
	implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.22")


	/***************************** Spring Cloud Starter Circuitbreaker Resilience4j ********************/
	/* Spring Cloud parent pom, managing plugins, and dependencies for Spring Cloud projects*/
	implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j:3.1.0")


	/***************************** Spring Cloud Starter Gateway ****************************************/
	/* Spring Cloud Starter */
	implementation("org.springframework.cloud:spring-cloud-starter-gateway-mvc:4.1.1")


	/***************************** Spring Boot Starter Data JPA ****************************************/
	/* Starter for using Spring Data JPA with Hibernate */
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.2.1")


	/***************************** H2 Database Engine **************************************************/
	/* A fast SQL database that can run embedded or a server mode with support for transactions, encryption, full search, etc.
	Storage can be disk-based or in-memory. */
	implementation("com.h2database:h2:2.2.224")


	/***************************** Spring Boot Starter Web ********************************************/
	/* Starter for building web, including RESTful, applications using Spring MVC. Uses Tomcat as the default embedded container */
	implementation("org.springframework.boot:spring-boot-starter-web:3.2.1")


	/***************************** Spring Boot DevTools ************************************************/
	/* Spring Boot Developer Tools*/
//	implementation( "org.springframework.boot:spring-boot-devtools:3.2.1")


	/***************************** Spring Boot Starter Test ********************************************/
	/* Starter for testing Spring Boot applications with libraries including JUnit Jupiter, Hamcrest and Mockito*/
	testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.1")

	/***************************** Kermit **************************************************************/
	/* Kermit The Log */
	implementation("co.touchlab:kermit:2.0.2")

	/***************************** Jackson Module Kotlin ***********************************************/
	/* Add-on module for Jackson (https://github.com/FasterXML/jackson/) to support Kotlin language,
	specifically introspection of method/constructor parameter names without having to add explicit property name annotation. */
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")

}

extra["springCloudVersion"] = "2023.0.0"
dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "21"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
