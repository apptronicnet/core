package net.apptronic.test.commons_sample_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.apptronic.core.android.viewmodel.activityContainer
import net.apptronic.test.commons_sample_app.app.getApplicationComponent

class MainActivity : AppCompatActivity() {

//    private val uiContainer by lazy {
//        activityContainer(
//            getApplicationComponent().appUI
//        )
//    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        uiContainer.onActivityCreate()
//    }
//
//    override fun onStart() {
//        super.onStart()
//        uiContainer.onActivityStart()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        uiContainer.onActivityResume()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        uiContainer.onActivityPause()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        uiContainer.onActivityStop()
//    }

//    override fun onBackPressed() {
//        getApplicationComponent().appUI.getViewModel().onBackPressed {
//            finish()
//        }
//    }

//    override fun onDestroy() {
//        super.onDestroy()
//        uiContainer.onActivityDestroy()
//    }

}
