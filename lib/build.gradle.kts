plugins {
    kotlin("jvm")
}

repositories {
    jcenter()
}

dependencies {
    val kotlinVersion = "1.3.21"
    val junitVersion = "4.12"
    implementation(kotlin("stdlib:$kotlinVersion"))
    implementation(kotlin("reflect:$kotlinVersion"))

    testImplementation("junit:junit:$junitVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")

}
