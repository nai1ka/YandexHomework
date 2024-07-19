package plugins

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import tasks.CheckArtifactsTask
import tasks.GenerateNameTask
import tasks.PrintHelloTask
import tasks.PrintUserNameTask
import tasks.SayHelloUserTask
import java.io.File

class MyPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val androidComponents = project.extensions.findByType(AndroidComponentsExtension::class.java)
            ?: throw GradleException("android plugin required.")

        androidComponents.onVariants { variant ->
            println("Variant ${variant.name}")
            val artifacts = variant.artifacts.get(SingleArtifact.APK)
            project.tasks.register("checkDirFor${variant.name}", CheckArtifactsTask::class) {
                apkDir.set(artifacts)
            }
        }

    }

    private fun Project.configureDemoTasks() {
        val printHelloTask = tasks.register("printHello", PrintHelloTask::class)
        val printUserNameTask = tasks.register("printUserName", PrintUserNameTask::class)
        printUserNameTask.dependsOn(printHelloTask)

        val userNameTask = tasks.register("userName", GenerateNameTask::class) {
            userNameFile.set(File("userName.txt"))
        }
        tasks.register("helloUser", SayHelloUserTask::class) {
            userNameFile.set(userNameTask.get().userNameFile)
        }
    }
}

