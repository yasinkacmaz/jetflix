import org.gradle.api.JavaVersion

object Config {
    const val minSdk = 21
    const val targetSdk = 33
    const val compileSdk = 33
    const val versionCode = 4
    const val versionName = "1.3.0"
    val javaVersion = JavaVersion.VERSION_11
}
