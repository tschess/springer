import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    kotlin("jvm") version "1.3.60"
    kotlin("kapt") version "1.3.60"
    kotlin("plugin.jpa") version "1.3.60"
    kotlin("plugin.spring") version "1.3.60"

    id("org.jetbrains.kotlin.plugin.allopen") version "1.3.60"
    id("org.springframework.boot") version "2.1.8.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

group = "io.bahlsenwitz"
version = "1.0.0"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.hibernate:hibernate-core:5.4.10.Final")
    implementation("com.vladmihalcea:hibernate-types-52:2.6.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.json:json:20171018")
    implementation("com.squareup.moshi:moshi-kotlin:1.8.0")
    implementation("org.apache.httpcomponents:httpclient:4.5.10")
    runtimeOnly("org.postgresql:postgresql:42.1.4")

    /* * */
    implementation("khttp:khttp:0.1.0")
    /* * */

    testImplementation("io.kotlintest:kotlintest-core:3.0.2")
    testImplementation("io.kotlintest:kotlintest-assertions:3.0.2")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.0.2")
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.2.4.RELEASE")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
    kotlinOptions.jvmTarget = "1.8"
}

