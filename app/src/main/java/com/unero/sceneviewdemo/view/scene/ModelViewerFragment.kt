package com.unero.sceneviewdemo.view.scene

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.filament.utils.HDRLoader
import com.unero.sceneviewdemo.R
import com.unero.sceneviewdemo.databinding.FragmentModelViewerBinding
import com.unero.sceneviewdemo.utils.NavigationFragment
import io.github.sceneview.SceneView
import io.github.sceneview.environment.loadEnvironment
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.delay

class ModelViewerFragment : Fragment(), NavigationFragment {

    private var _binding: FragmentModelViewerBinding? = null
    private val binding get() = _binding as FragmentModelViewerBinding

    private lateinit var sceneView: SceneView
    private val viewModel: ArViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setLoading(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModelViewerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabBack.setOnClickListener { popNavigation(it) }

        sceneView = binding.sceneView
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.loadingView.isGone = !it
        }

        sceneView.camera.apply {
            position = Position(x = 4.0f, y = -1.0f)
            rotation = Rotation(x = 0.0f, y = 80.0f)
        }

        val modelNode = ModelNode()
        sceneView.addChild(modelNode)

        lifecycleScope.launchWhenCreated {
            sceneView.environment = HDRLoader.loadEnvironment(
                context = requireContext(),
                lifecycle = lifecycle,
                hdrFileLocation = "environments/studio_small_09_2k.hdr",
                specularFilter = false
            )

            modelNode.loadModel(
                context = requireContext(),
                lifecycle = lifecycle,
                glbFileLocation = "https://sceneview.github.io/assets/models/MaterialSuite.glb",
                autoAnimate = true,
                autoScale = true,
                centerOrigin = Position(x = 0.0f, y = 0.0f, z = 0.0f)
            )

            delay(200)
            viewModel.setLoading(false)
            sceneView.camera.smooth(
                position = Position(x = -1.0f, y = 1.5f, z = -3.5f),
                rotation = Rotation(x = -60.0f, y = -50.0f),
                speed = 0.5f
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}