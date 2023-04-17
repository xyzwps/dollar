plugins {
    id("java")
    `maven-publish`
}

group = "com.xyzwps.lib"
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

java {
    withJavadocJar()
    withSourcesJar()
}

// see https://docs.gradle.org/current/userguide/publishing_maven.html
// see https://zhuanlan.zhihu.com/p/393031307
// see https://blog.csdn.net/wangxudongx/article/details/119513838
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            group = project.group
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
            pom {
                name.set("xyzwps-dollar")
                description.set("A lodash-like, lazy and powerful java collection utils")
                url.set("https://github.com/xyzwps/dollar")
                developers {
                    developer {
                        id.set("xyzwps")
                        name.set("Wei Liangyu")
                        email.set("xyzwps@outlook.com")
                    }
                }
            }
        }
    }
    repositories {
        maven {
            name = "OSSRH"
            if (project.version.toString().endsWith("-SNAPSHOT")) {
                setUrl("https://s01.oss.sonatype.org/content/repositories/snapshots")
            } else {
                setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            }
            credentials {
                username = System.getenv("OSSRH_USERNAME")
                password = System.getenv("OSSRH_PASSWORD")
            }
        }
    }
}
