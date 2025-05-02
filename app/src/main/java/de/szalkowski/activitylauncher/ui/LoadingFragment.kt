package de.szalkowski.activitylauncher.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withResumed
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import de.szalkowski.activitylauncher.R
import de.szalkowski.activitylauncher.services.PackageListService
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider
import kotlin.concurrent.thread

@AndroidEntryPoint
class LoadingFragment : Fragment() {
    @Inject
    internal lateinit var packageListService: Provider<PackageListService>

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        lifecycleScope.launch {
        withResumed {
            // preload package list on background thread, but switch back to main thread to navigate
            withContext(Dispatchers.IO) {
                packageListService.get()
            }
            val action = LoadingFragmentDirections.actionLoadingFinished()
            findNavController().navigate(action)
        }
    }
        
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_loading, container, false)
    
    }    
}
