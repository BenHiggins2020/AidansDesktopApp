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
