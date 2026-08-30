plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "psylos.morphe.extension.soundcloud.stub"
    compileSdk = 36

    defaultConfig {
        minSdk = 32
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
