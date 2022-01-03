plugins {
    id("fabric-loom") version "0.10-SNAPSHOT"
    id("io.github.juuxel.loom-quiltflower-mini") version "1.2.1"
    id("org.quiltmc.quilt-mappings-on-loom") version "3.1.1"
    id("org.cadixdev.licenser") version "0.6.1"
}

val modVersion: String by project

group = "io.github.jamalam360"
version = modVersion

repositories {
    val mavenUrls = listOf(
        "https://maven.terraformersmc.com/releases",
        "https://maven.shedaniel.me/",
        "https://jitpack.io",
        "https://repo1.maven.org/maven2/"
    )

    for (url in mavenUrls) {
        maven(url = url)
    }
}

dependencies {
    val minecraftVersion: String by project
    val mappingsVersion: String by project
    val loaderVersion: String by project
    val fabricApiVersion: String by project
    val clothConfigVersion: String by project
    val modMenuVersion: String by project
    val enumExtenderVersion: String by project
    val junitVersion: String by project

    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.layered {
        addLayer(quiltMappings.mappings("org.quiltmc:quilt-mappings:$minecraftVersion+build.$mappingsVersion:v2"))
    })

    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
    modImplementation("com.terraformersmc:modmenu:$modMenuVersion")
    modImplementation("com.github.alkyaly:enum-extender-3000:$enumExtenderVersion")
    include("com.github.alkyaly:enum-extender-3000:$enumExtenderVersion")

    modApi("me.shedaniel.cloth:cloth-config-fabric:$clothConfigVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(
                mutableMapOf(
                    "version" to project.version
                )
            )
        }
    }

    build {
        dependsOn("updateLicenses")
    }

    jar {
        archiveBaseName.set("Notify+1.17")
    }

    remapJar {
        archiveBaseName.set("Notify+1.17")
    }

    withType<JavaCompile> {
        options.release.set(17)
    }
}
