rootProject.name = "MasteringGradleDemo"
include(":app", ":mylibrary1", ":myLib1", ":myLib2", ":myLib3", ":platform")

//获取全局属性
val gradleVersion = gradle.gradleVersion
val gradleDir = gradle.gradleHomeDir
val userDir = gradle.gradleUserHomeDir
println("gradleVersion:$gradleVersion")
println("gradle_dir:$gradleDir")
println("user_home_dir:$userDir")
println("rootProject:${rootProject.name}")
//为全部模块配置插件仓库
//gradle.settingsEvaluated {
//    pluginManagement {
//        repositories {
//            gradlePluginPortal()
//            google()
//            mavenCentral()
//        }
//    }
//}

//gradle 7.x后使用pluginManagement替换根项目build.gradle中的buildScrip，管理项目插件依赖（plugins部分）
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
    resolutionStrategy {
        eachPlugin {
            println("插件名称：${requested.id.id}:${requested.version ?: ""}")
            //强制某个插件使用指定版本号
            if (requested.id.namespace == "com.android") {
                useModule("com.android.tools.build:gradle:7.3.0")
            }
            //或
            if (requested.id.id == "org.jetbrains.kotlin.android") {
                useVersion("1.6.10")
            }
        }
    }
}
//gradle 7.0使用dependencyResolutionManagement替换根项目build.gradle中的allProjects，管理各模块lib依赖(dependencies部分)
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(
            url = "http://192.168.21.229:8081/repository/maven-releases/"
        ) {
            isAllowInsecureProtocol = true
        }
    }
}

//打印配置顺序，顺便改变一下默认按模块字母排序的顺序，优先配置myLib2模块
gradle.projectsLoaded {
    allprojects {
        //改变依赖顺序(需要在beforeEvaluated时机)
        beforeEvaluate {
            println("开始配置模块：$name")
            if (name == "myLib1") {
                evaluationDependsOn(":myLib2")
            }
        }
    }
}
//在这个生命周期，可以对所有project进行再次配置
gradle.beforeProject {
    println("开始配置模块2")
}

gradle.addListener(object : TaskExecutionListener {
    override fun beforeExecute(task: Task) {
        println("Task：${task.name}开始执行")
    }

    override fun afterExecute(task: Task, state: TaskState) {
        println("Task：${task.name}执行完毕")
    }
})

gradle.taskGraph.addTaskExecutionGraphListener { graph ->
    for (task in graph.allTasks) {
        println("Task：${task.name}准备就绪")
    }
}
