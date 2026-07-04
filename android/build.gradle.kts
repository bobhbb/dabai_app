allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

val newBuildDir: Directory =
    rootProject.layout.buildDirectory
        .dir("../../build")
        .get()
rootProject.layout.buildDirectory.value(newBuildDir)

subprojects {
    val newSubprojectBuildDir: Directory = newBuildDir.dir(project.name)
    project.layout.buildDirectory.value(newSubprojectBuildDir)
}
subprojects {
    project.evaluationDependsOn(":app")
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}


# 备份原文件
Copy-Item android/build.gradle.kts android/build.gradle.bak

# 读取文件加镜像
$buildGradle = Get-Content android/build.gradle.kts -Raw

$mirrorScript = @'
allprojects {
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        google()
        mavenCentral()
    }
}
'@

# 插入到 plugins 块之后
$newContent = $buildGradle -replace '(plugins \{[^}]*\})', "`$1`n`n$mirrorScript"
Set-Content android/build.gradle.kts $newContent -Encoding UTF8