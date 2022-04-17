package com.unero.sceneviewdemo.view.scene

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.unero.sceneviewdemo.R
import com.unero.sceneviewdemo.databinding.FragmentArModelViewerBinding
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.EditableTransform
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position

class ArModelViewerFragment : Fragment() {

    private var _binding: FragmentArModelViewerBinding? = null
    private val binding get() = _binding as FragmentArModelViewerBinding

    private lateinit var sceneView: ArSceneView
    private lateinit var actionButton: ExtendedFloatingActionButton

    private lateinit var modelNode: ArModelNode

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArModelViewerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init Views
        sceneView = binding.sceneView
        actionButton = binding.fabAction

        actionButton.setOnClickListener { actionButtonClicked() }

        modelNode = ArModelNode(placementMode = PlacementMode.BEST_AVAILABLE).apply {
            loadModelAsync(
                context = requireContext(),
                lifecycle = lifecycle,
                glbFileLocation = "models/bonnie.glb",
                autoAnimate = true,
                autoScale = false,
                centerOrigin = Position(y = -1.0f)
            )
            onTrackingChanged = { _, isTracking, _ ->
                actionButton.isGone = !isTracking
            }
            editableTransforms = EditableTransform.ALL
        }
        sceneView.addChild(modelNode)
        sceneView.gestureDetector.onTouchNode(modelNode)
    }

    private fun actionButtonClicked() {
        if(!modelNode.isAnchored && modelNode.anchor()) {
            actionButton.apply {
                text = "Move"
                setIconResource(R.drawable.ic_move)
            }
            sceneView.planeRenderer.isVisible = false
        } else {
            modelNode.anchor = null
            actionButton.apply {
                text = "Anchor"
                setIconResource(R.drawable.ic_anchor)
            }
            sceneView.planeRenderer.isVisible = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        modelNode.detachAnchor()
        _binding = null
    }
}