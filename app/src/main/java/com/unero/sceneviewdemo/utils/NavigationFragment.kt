package com.unero.sceneviewdemo.utils

import android.view.View
import androidx.navigation.findNavController

interface NavigationFragment {
    fun popNavigation(view: View) {
        view.findNavController().popBackStack()
    }
}