import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    var kotlin_version: String by extra
    kotlin_version = "1.2.41"

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(kotlinModule("gradle-plugin", kotlin_version))
    }
}

plugins {
    val kotlinVersion = "1.2.41"
    java
    application

    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
    id("io.spring.dependency-management") version "1.0.4.RELEASE"
    id("org.springframework.boot") version "2.0.1.RELEASE"
}

group = "com.jess.personal"
version = "1.0-SNAPSHOT"

apply {
    plugin("kotlin")
}

repositories {
    mavenCentral()
}

dependencies {
//    compile("io.reactivex:rxkotlin:2.2.0")
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:0.22.5")

    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compile("org.jetbrains.kotlin:kotlin-reflect")

    compile("org.springframework.boot:spring-boot-starter-web")
    compile("org.springframework:spring-webflux")
    compile("org.springframework:spring-context") {
        exclude(module = "spring-aop")
    }
    compile("io.projectreactor.ipc:reactor-netty")
    compile("com.samskivert:jmustache")

    compile("org.slf4j:slf4j-api")
    compile("ch.qos.logback:logback-classic")

    compile("com.fasterxml.jackson.module:jackson-module-kotlin")
    compile("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    testCompile("io.projectreactor:reactor-test")
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
