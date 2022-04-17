package com.unero.sceneviewdemo.view.scene

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.ar.core.Anchor
import com.unero.sceneviewdemo.R
import com.unero.sceneviewdemo.databinding.FragmentArCursorBinding
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.CursorNode
import io.github.sceneview.math.Position

class ArCursorFragment : Fragment() {

    private var _binding: FragmentArCursorBinding? = null
    private val binding get() = _binding as FragmentArCursorBinding

    private lateinit var sceneView: ArSceneView
    private lateinit var actionButton: ExtendedFloatingActionButton

    private lateinit var cursorNode: CursorNode
    private lateinit var modelNode: ArModelNode

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArCursorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sceneView = binding.sceneView
        actionButton = binding.fabAction

        actionButton.setOnClickListener { cursorNode.createAnchor()?.let { anchorOrMove(it) } }

        sceneView.apply {
            planeRenderer.isVisible = false
            onArSessionFailed = {
                modelNode.centerModel(origin = Position(x = 0.0f, y = 0.0f, z = 0.0f))
                modelNode.scaleModel(units = 1.0f)
                sceneView.addChild(modelNode)
            }
            onTouchAr = { hitResult, _ ->
                anchorOrMove(hitResult.createAnchor())
            }
        }

        cursorNode = CursorNode(requireContext(), lifecycle).apply {
            onTrackingChanged = { _, isTracking, _ ->
                actionButton.isEnabled = !isTracking
            }
        }
        sceneView.addChild(cursorNode)

        modelNode = ArModelNode()
        modelNode.loadModelAsync(
            context = requireContext(),
            lifecycle = lifecycle,
            glbFileLocation = "models/bonnie.glb",
            onLoaded = {
                actionButton.apply {
                    text = "Move"
                    setIconResource(R.drawable.ic_sceneview)
                }
            }
        )
    }

    private fun anchorOrMove(anchor: Anchor) {
        if (!sceneView.children.contains(modelNode)) {
            sceneView.addChild(modelNode)
        }
        modelNode.anchor = anchor
    }

    override fun onDestroyView() {
        super.onDestroyView()
        modelNode.detachAnchor()
        cursorNode.detachAnchor()
        _binding = null
    }
}