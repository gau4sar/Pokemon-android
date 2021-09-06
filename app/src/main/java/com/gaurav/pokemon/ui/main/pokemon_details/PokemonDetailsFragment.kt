package com.gaurav.pokemon.ui.main.pokemon_details

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gaurav.pokemon.R
import com.gaurav.pokemon.adapter.PokemonTypeAdapter
import com.gaurav.pokemon.data.model.CapturePokemon
import com.gaurav.pokemon.data.model.Pokemon
import com.gaurav.pokemon.data.model.PokemonCapture
import com.gaurav.pokemon.data.model.PokemonDetails
import com.gaurav.pokemon.data.model.pokemon.Type
import com.gaurav.pokemon.databinding.FragmentPokemonDetailsBinding
import com.gaurav.pokemon.ui.main.MainViewModel
import com.gaurav.pokemon.utils.Constants.POKEMON_CAPTURED
import com.gaurav.pokemon.utils.Constants.POKEMON_CAPTURED_BY_OTHER
import com.gaurav.pokemon.utils.Constants.POKEMON_DETAILS
import com.gaurav.pokemon.utils.Constants.POKEMON_WILD
import com.gaurav.pokemon.utils.GeneralUtils.parseDateToShortMonthDateAndYear
import com.gaurav.pokemon.utils.load
import com.gaurav.pokemon.utils.make1stCharacterUpper
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
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber
import kotlin.math.abs


class PokemonDetailsFragment : Fragment(R.layout.fragment_pokemon_details) {

    private val mainViewModel by sharedViewModel<MainViewModel>()
    private val gson: Gson by inject(Gson::class.java)

    private lateinit var googleMap: GoogleMap
    private lateinit var callback: OnMapReadyCallback
    private lateinit var pokemonDetails: PokemonDetails

    private var _binding: FragmentPokemonDetailsBinding? = null
    private val binding get() = _binding!!

    private var isWild = false
    private var isCaptured = false
    private var isCapturedByOther = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.llProgressBar.visibility = View.VISIBLE

        requireActivity().intent?.extras?.let { it ->

            it.getSerializable(POKEMON_DETAILS)?.let {
                val type = object : TypeToken<PokemonDetails>() {}.type
                val pokemonDetailsString = gson.toJson(it, type)
                pokemonDetails = gson.fromJson(pokemonDetailsString, type)

                Timber.d("pokemonDetails : $pokemonDetails")

                when (pokemonDetails.pokemonStatus) {
                    POKEMON_WILD -> {
                        isWild = true
                    }

                    POKEMON_CAPTURED -> {
                        isCaptured = true
                    }

                    POKEMON_CAPTURED_BY_OTHER -> {
                        isCapturedByOther = true
                    }
                }

                val pokemonLocationInfo = pokemonDetails.pokemonLocationInfo
                mainViewModel.fetchPokemonDetails(pokemonLocationInfo.name.lowercase())

                setMarker(
                    LatLng(
                        pokemonLocationInfo.capturedLatAt,
                        pokemonLocationInfo.capturedLongAt
                    )
                )
            }

            // Observer for getting pokemon details information
            mainViewModel.pokemonLiveData.observe(requireActivity(), {
/*
                Timber.d("Pokemon details response : $it")
*/
                it?.let { setupUi(it) }
            })
        }

        viewModelWorks()

    }

    private fun viewModelWorks() {

        mainViewModel.capturePokemonLiveData.observe(requireActivity(), { isSuccess ->

            animation(isSuccess)
        })
    }

    private fun animation(isSuccess: Boolean) {
        binding.clPokeballStatus.visibility = View.VISIBLE


        val red = ContextCompat.getDrawable(requireActivity(), R.color.color_red)
        val white = ContextCompat.getDrawable(requireActivity(), R.color.white)
        val blue = ContextCompat.getDrawable(requireActivity(), R.color.light_blue_A400)

        /*val animation: Animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_blink)
        binding.ivPokeballStatus.startAnimation(animation)
*/

        //TODO find another way for animation
        Handler(Looper.getMainLooper()).postDelayed(
            {
                binding.ivPokeballStatus.background = red
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        binding.ivPokeballStatus.background = white
                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                binding.ivPokeballStatus.background = red

                                Handler(Looper.getMainLooper()).postDelayed(
                                    {
                                        binding.ivPokeballStatus.background = white
                                        Handler(Looper.getMainLooper()).postDelayed(
                                            {
                                                binding.ivPokeballStatus.background = blue

                                                if (isSuccess) {
                                                    binding.btnCapture.visibility =
                                                        View.GONE
                                                    binding.fabCapture.visibility =
                                                        View.VISIBLE
                                                }
                                                else{
                                                    binding.ivPokeballStatus.background = red
                                                }
                                                Handler(Looper.getMainLooper()).postDelayed(
                                                    {
                                                        binding.clPokeballStatus.visibility =
                                                            View.GONE
                                                    },
                                                    1500
                                                )
                                            },
                                            1000
                                        )
                                    },
                                    500
                                )
                            },
                            1000
                        )
                    },
                    500
                )
            },
            1000
        )

    }

    private fun setupUi(pokemon: Pokemon) {
        Timber.d("setupUi $pokemon")

        val captureButton: MaterialButton = binding.btnCapture
        val tvCaptureDate = binding.layoutBasicInfo.tvCaptureDate
        val captureLayout = binding.layoutBasicInfo.llCaptureOn
        val ivPokemonFront = binding.ivPokemonFront
        val ivPokemonBack = binding.ivPokemonBack
        val tvFoundCaptured: TextView = binding.layoutMap.tvFoundInCapturedIn
        val pokemonCaptured: com.github.clans.fab.FloatingActionButton = binding.fabCapture
        val layoutMap: CardView = binding.layoutMap.cvFoundCaptured
        val layoutCapturedBy: CardView = binding.layoutCapturedBy.cvCapturedBy
        val appBarLayout: AppBarLayout = binding.appBarLayout
        val pokemonInToolbar: ImageView = binding.ivPokemonToolbar
        val tvCapturedBy = binding.layoutCapturedBy.tvName
        val tvLevel = binding.layoutBasicInfo.tvLevel

        setupRecyclerView(pokemon.types)

        ivPokemonFront.load(pokemon.sprites.frontDefault, requireActivity())
        ivPokemonBack.load(pokemon.sprites.backDefault, requireActivity())

        binding.collapsingToolbar.title = pokemon.name.make1stCharacterUpper()

        pokemon.types

        val collapsingToolbar = binding.collapsingToolbar
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)

        tvLevel.text = pokemon.id.toString()
        tvCapturedBy.text = pokemonDetails.capturedByName
        tvCaptureDate.text =
            parseDateToShortMonthDateAndYear(pokemonDetails.pokemonLocationInfo.capturedAt)

        if (isWild) {
            captureLayout.visibility = View.GONE
            pokemonCaptured.visibility = View.GONE
            captureButton.visibility = View.VISIBLE
        } else {
            captureLayout.visibility = View.VISIBLE
            pokemonCaptured.visibility = View.VISIBLE
            captureButton.visibility = View.GONE
            tvFoundCaptured.text = getString(R.string.captured_in)
        }

        if (isCapturedByOther) {

            pokemonCaptured.visibility = View.GONE
            pokemonInToolbar.visibility = View.GONE
            captureButton.visibility = View.GONE
            tvFoundCaptured.text = getString(R.string.captured_in)
            layoutMap.visibility = View.GONE
            layoutCapturedBy.visibility = View.VISIBLE
        }

        if (!isCapturedByOther && !isWild) {
            appBarLayout.addOnOffsetChangedListener(OnOffsetChangedListener { barLayout, verticalOffset ->
                if (abs(verticalOffset) - barLayout.totalScrollRange == 0) {
                    pokemonCaptured.visibility = View.GONE
                    pokemonInToolbar.visibility = View.VISIBLE
                } else {
                    pokemonCaptured.visibility = View.VISIBLE
                    pokemonInToolbar.visibility = View.GONE
                }
            })
        }

        captureButton.setOnClickListener {
            showCaptureDialog(pokemon)
        }

        binding.llProgressBar.visibility = View.GONE
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

    private fun setupRecyclerView(pokemonType: List<Type>) {

        val pokemonTypeAdapter =
            context?.let {
                PokemonTypeAdapter(
                    requireActivity(),
                    pokemonType
                )
            }

        binding.layoutBasicInfo.recyclerViewTypes.apply {
            adapter = pokemonTypeAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    /**
     * Displays pop-up dialog to confirm capture
     */

    private fun showCaptureDialog(pokemon: Pokemon) {
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
                binding.clPokeballStatus.visibility = View.VISIBLE
                val pokemonLocationInfo = pokemonDetails.pokemonLocationInfo
                val capturePokemon = CapturePokemon(
                    PokemonCapture(
                        pokemon.id,
                        etName.editableText.toString(),
                        pokemonLocationInfo.capturedLatAt.toLong(),
                        pokemonLocationInfo.capturedLongAt.toLong()
                    )
                )
                mainViewModel.capturePokemon(capturePokemon)
                dialog.dismiss()
            }
        }
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        resources.let {
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