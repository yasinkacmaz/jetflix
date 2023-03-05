val ktlint: Configuration by configurations.creating

dependencies {
    ktlint("com.pinterest:ktlint:0.48.2")
}

val ktlintCheck by createKtlintTask(
    taskDescription = "Check Kotlin code style.",
    taskArgs = listOf("*.kt*", "--reporter=plain", "--reporter=checkstyle,output=${buildDir}/ktlint-report.xml")
)

val ktlintFormat by createKtlintTask(
    taskDescription = "Fix Kotlin code style deviations.",
    taskArgs = listOf("-F", "*.kt*")
)

fun createKtlintTask(taskDescription: String, taskArgs: List<String>) = tasks.creating(JavaExec::class) {
    group = "ktlint"
    description = taskDescription
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args = taskArgs
    // https://github.com/pinterest/ktlint/issues/1195#issuecomment-1009027802
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
}
