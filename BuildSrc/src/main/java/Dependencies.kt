
object Versions{
    const val appCompatVersion = "28.0.0-rc02"
    const val constrainVersion = "1.1.2"
    const val junitVersion = "4.12"
    const val testRunner = "1.0.2"
    const val esspressoCoreVersion = "3.0.2"
    const val firebaseCoreVeresions = "16.0.3"
    const val machineLearningVersion = "17.0.0"
    const val kotlinVersion = "1.2.51"
    const val glideVersion = "4.7.1"
    const val dexterVersion = "5.0.0"
}

object Deps {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlinVersion}"
    const val appCompat = "com.android.support:appcompat-v7:${Versions.appCompatVersion}"
    const val support = "com.android.support:support-v4:${Versions.appCompatVersion}"
    const val constraintLayout = "com.android.support.constraint:constraint-layout:${Versions.constrainVersion}"
    const val junit = "junit:junit:${Versions.junitVersion}"
    const val testRunner = "com.android.support.test:runner:${Versions.testRunner}"
    const val esspressoCore = "com.android.support.test.espresso:espresso-core:${Versions.esspressoCoreVersion}"

    const val firebaseCore = "com.google.firebase:firebase-core:${Versions.firebaseCoreVeresions}"
    const val machineLearning = "com.google.firebase:firebase-ml-vision:${Versions.machineLearningVersion}"

    const val glideCore = "com.github.bumptech.glide:glide:${Versions.glideVersion}"
    const val glideApt = "com.github.bumptech.glide:compiler:${Versions.glideVersion}"
    const val dexter =  "com.karumi:dexter:${Versions.dexterVersion}"
}