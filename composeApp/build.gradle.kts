import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation("org.jsoup:jsoup:+")
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.15.3") // XML serialization
            implementation("org.apache.poi:poi-ooxml:5.2.0")
            implementation("io.reactivex.rxjava3:rxkotlin:3.0.1")
            implementation("io.reactivex.rxjava3:rxandroid:3.0.0") // For Android UI thread handling
            implementation("com.squareup.okhttp3:okhttp:4.9.3") // Use the latest version
            implementation("io.ktor:ktor-server-core:2.3.1")
            implementation("io.ktor:ktor-server-netty:2.3.1")
            implementation("io.ktor:ktor-client-core:2.3.1")
            implementation("io.ktor:ktor-client-cio:2.3.1") // CIO engine for HTTP requests
            implementation("com.squareup.okhttp3:okhttp:4.12.0")
            implementation("org.seleniumhq.selenium:selenium-java:4.20.0") // this is for the headless browser
            implementation("io.github.bonigarcia:webdrivermanager:5.8.0") // this will automatically download the correct chrome driver.


        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation("org.mockito.kotlin:mockito-kotlin:5.1.0") // Kotlin-friendly version
            implementation("org.mockito:mockito-core:5.3.1") // Adjust version if needed
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}


compose.desktop {
    application {
        mainClass = "com.ben.aidansdesktopapp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.ben.aidansdesktopapp"
            packageVersion = "1.0.0"
        }
    }
}
