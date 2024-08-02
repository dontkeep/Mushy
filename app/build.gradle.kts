plugins {
   alias(libs.plugins.android.application)
   alias(libs.plugins.jetbrains.kotlin.android)
   id("kotlin-kapt")
}

android {
   namespace = "com.loyalty.mushy"
   compileSdk = 34

   defaultConfig {
      applicationId = "com.loyalty.mushy"
      minSdk = 24
      targetSdk = 34
      versionCode = 1
      versionName = "1.0"

      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
   }

   buildFeatures {
      viewBinding = true
   }

   buildTypes {
      release {
         isMinifyEnabled = false
         proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
         )
      }
   }
   compileOptions {
      sourceCompatibility = JavaVersion.VERSION_1_8
      targetCompatibility = JavaVersion.VERSION_1_8
   }
   kotlinOptions {
      jvmTarget = "1.8"
   }
}

dependencies {
   implementation("de.hdodenhof:circleimageview:3.1.0")
   implementation("com.github.AppIntro:AppIntro:6.3.1")
   implementation("org.tensorflow:tensorflow-lite:2.16.1")
   implementation("org.tensorflow:tensorflow-lite:0.0.0-nightly")
   implementation("androidx.compose.material3:material3:1.2.1")
   implementation("androidx.navigation:navigation-fragment-ktx:2.5.3") // Use the latest version
   implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
   implementation(libs.androidx.core.ktx)
   implementation(libs.androidx.appcompat)
   implementation(libs.material)
   implementation(libs.androidx.activity)
   implementation(libs.androidx.constraintlayout)
   implementation(libs.androidx.navigation.fragment.ktx)
   implementation(libs.androidx.navigation.ui.ktx)
   testImplementation(libs.junit)
   androidTestImplementation(libs.androidx.junit)
   androidTestImplementation(libs.androidx.espresso.core)
}