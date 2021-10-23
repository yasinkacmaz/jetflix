package plugins

val ktlint: Configuration by configurations.creating

dependencies {
    ktlint(Dependencies.ktlint)
}

val ktlintCheck by createKtlintTask(
    taskDescription = "Check Kotlin code style.",
    taskArgs = listOf("src/**/*.kt", "--reporter=checkstyle,output=${buildDir}/ktlint-report.xml")
)

val ktlintFormat by createKtlintTask(
    taskDescription = "Fix Kotlin code style deviations.",
    taskArgs = listOf("-F", "src/**/*.kt")
)

fun createKtlintTask(taskDescription: String, taskArgs: List<String>) = tasks.creating(JavaExec::class) {
    val inputFiles: ConfigurableFileTree = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))
    inputs.files(inputFiles)
    group = "ktlint"
    description = taskDescription
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args = taskArgs
}
