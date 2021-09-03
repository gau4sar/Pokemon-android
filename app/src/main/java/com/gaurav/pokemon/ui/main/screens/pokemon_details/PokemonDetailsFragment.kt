package com.gaurav.pokemon.ui.main.screens.pokemon_details

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.gaurav.pokemon.R
import com.gaurav.pokemon.data.model.PokemonFound
import com.gaurav.pokemon.databinding.*
import com.gaurav.pokemon.ui.main.MainViewModel
import com.gaurav.pokemon.utils.Constants.POKEMON_FOUND
import com.gaurav.pokemon.utils.showToast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class PokemonDetailsFragment : Fragment(R.layout.fragment_pokemon_details) {

    private val mainViewModel by sharedViewModel<MainViewModel>()
    private lateinit var googleMap: GoogleMap

    lateinit var callback: OnMapReadyCallback

    private var _binding: FragmentPokemonDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().intent?.let {
            it.getParcelableExtra<PokemonFound>(POKEMON_FOUND)?.let {pokemonFound->

                Timber.d("POKEMON_FOUND $pokemonFound")

                setMarker(pokemonFound.pokemon_location)

                initializeTheUi(pokemonFound)

                viewModelWorks(pokemonFound.pokemon_id)
            }
        }
    }

    private fun initializeTheUi(pokemonFound: PokemonFound) {
        Timber.d("pokemonFound $pokemonFound")

        val captureButton: MaterialButton = binding.btnCapture
        val tvFoundCaptured: TextView = binding.layoutMap.tvFoundInCapturedIn
        val pokemonCaptured: com.github.clans.fab.FloatingActionButton =binding.fabCapture
        val layoutMap: CardView = binding.layoutMap.cvFoundCaptured
        val layoutCapturedBy: CardView = binding.layoutCapturedBy.cvCapturedBy
        val appBarLayout: AppBarLayout = binding.appBarLayout
        val pokemonInToolbar: ImageView = binding.ivPokemonToolbar

        val collapsingToolbar = binding.collapsingToolbar
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)

        if (pokemonFound.isWild) {
            pokemonCaptured.visibility = View.GONE
            captureButton.visibility = View.VISIBLE
        } else {
            pokemonCaptured.visibility = View.VISIBLE
            captureButton.visibility = View.GONE
            tvFoundCaptured.text = getString(R.string.captured_in)
        }

        if (pokemonFound.isCapturedByOther) {
            pokemonCaptured.visibility = View.GONE
            pokemonInToolbar.visibility = View.GONE
            captureButton.visibility = View.GONE
            tvFoundCaptured.text = getString(R.string.captured_in)
            layoutMap.visibility = View.GONE
            layoutCapturedBy.visibility = View.VISIBLE
        }

        if(!pokemonFound.isCapturedByOther&&!pokemonFound.isWild) {
            appBarLayout.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (Math.abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                    pokemonCaptured.visibility = View.GONE
                    pokemonInToolbar.visibility = View.VISIBLE
                } else {
                    pokemonCaptured.visibility = View.VISIBLE
                    pokemonInToolbar.visibility = View.GONE
                }
            })
        }

        captureButton.setOnClickListener {
            showCaptureDialog()
        }

    }

    private fun setMarker(position: LatLng) {

        callback = OnMapReadyCallback { _googleMap ->
            val cameraPosition = CameraPosition.Builder()
                .target(position)
                .zoom(13f)
                .build()

            _googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            _googleMap.addMarker(
                MarkerOptions()
                    .position(position)
                    .title("Pokemon")
                    .icon(
                        BitmapDescriptorFactory
                            .fromResource(R.drawable.icons8_pokeball_96)
                    )
            )
            googleMap = _googleMap
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }

    private fun viewModelWorks(pokemonId: Int) {

    }

    fun showCaptureDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_assign_name_to_pokemon)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        val cancelButton = dialog.findViewById(R.id.btn_cancel) as MaterialButton
        val saveButton = dialog.findViewById(R.id.btn_save) as MaterialButton
        val etName: EditText = dialog.findViewById(R.id.et_name) as EditText

        saveButton.setOnClickListener {
            if (etName.editableText.isNullOrEmpty()) {
                requireActivity().showToast("Please assign a name!")
            } else {
                dialog.dismiss()
            }
        }
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        resources?.let {
            val displayMetrics = it.displayMetrics
            val dialogWidth = (displayMetrics.widthPixels * 0.70).toInt()
            val dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(dialogWidth, dialogHeight)
            dialog.show()
        }
        dialog.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                requireActivity(),
                android.R.color.transparent
            )
        )

        dialog.show()
    }


}