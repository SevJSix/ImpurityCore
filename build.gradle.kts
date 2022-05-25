plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://repo.maven.apache.org/maven2/") }
    maven { url = uri("https://repo.txmc.me/releases") }
}

dependencies {
    implementation("me.txmc:protocolapi:1.2-SNAPSHOT")
    implementation("me.txmc:rtmixin:1.3-BETA")
    compileOnly("com.destroystokyo.paper:paper-jar:1.12.2-R0.1-SNAPSHOT")
    compileOnly("de.tr7zw:item-nbt-api-plugin:2.9.2")
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
}

tasks.shadowJar {
    manifest {
        attributes(
            "Manifest-Version" to "1.0",
            "Premain-Class" to "me.txmc.rtmixin.jagent.AgentMain",
            "Agent-Class" to "me.txmc.rtmixin.jagent.AgentMain",
            "Can-Redefine-Classes" to "true",
            "Can-Retransform-Classes" to "true",
            "Can-Set-Native-Method-Prefix" to "true"
        )
    }
    exclude("pom.*")
    minimize()
}

group = "me.sevj6"
version = "3.0-REWRITE"
description = "ImpurityCore"
java.sourceCompatibility = JavaVersion.VERSION_1_8

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
