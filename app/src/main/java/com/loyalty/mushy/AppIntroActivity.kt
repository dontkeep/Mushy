package com.loyalty.mushy

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType

class AppIntroActivity : AppIntro() {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setImmersiveMode()
      setTransformer(AppIntroPageTransformerType.Fade)
      isIndicatorEnabled = true
      setIndicatorColor(
         selectedIndicatorColor = resources.getColor(R.color.pastel_red),
         unselectedIndicatorColor = resources.getColor(R.color.pastel_green)
      )
      isColorTransitionsEnabled = true
      addSlide(AppIntroFragment.createInstance(
         title = "Welcome to Mushy!",
         description = "An app to help you identify whether or not a mushroom is edible!",
         imageDrawable = R.drawable.logo,
         backgroundColorRes = R.color.gray
      ))

      addSlide(AppIntroFragment.createInstance(
         title = "Helps you on your adventure!",
         description = "Simply tell the app when the mushroom looks like and let us handle the rest!",
         imageDrawable = R.drawable.magnify,
         backgroundColorRes = R.color.black_calm
      ))
   }

   override fun onSkipPressed(currentFragment: Fragment?) {
      super.onSkipPressed(currentFragment)
      //intent go to main activity
      val intent = Intent(this, MainActivity::class.java)
      startActivity(intent)
   }

   override fun onDonePressed(currentFragment: Fragment?) {
      super.onDonePressed(currentFragment)
      val intent = Intent(this, MainActivity::class.java)
      startActivity(intent)
   }
}