plugins {
    id("java")
}

group = "com.xyzwps"
version = "0.0.1"

repositories {
    maven {
        setUrl("https://maven.aliyun.com/repository/public/")
    }
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}