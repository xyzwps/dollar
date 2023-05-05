plugins {
    id("java")
    `maven-publish`
    signing
}

group = "com.xyzwps.lib"
version = "0.0.4"
java.sourceCompatibility = JavaVersion.VERSION_1_8

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

// see https://zhuanlan.zhihu.com/p/393031307
// gpg see https://zhuanlan.zhihu.com/p/354427909
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
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://github.com/xyzwps/dollar/blob/master/LICENSE")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com:xyzwps/dollar.git")
                    developerConnection.set("scm:git:ssh://github.com:xyzwps/dollar.git")
                    url.set("https://github.com/xyzwps/dollar")
                }
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

signing {
    sign(publishing.publications["mavenJava"])
}
