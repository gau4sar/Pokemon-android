package com.gaurav.pokemon.ui.pokemon_details

import android.app.Dialog
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gaurav.pokemon.R
import com.gaurav.pokemon.adapter.PokemonTypeAdapter
import com.gaurav.pokemon.data.model.CapturePokemon
import com.gaurav.pokemon.data.model.Pokemon
import com.gaurav.pokemon.data.model.PokemonCapture
import com.gaurav.pokemon.data.model.PokemonDetails
import com.gaurav.pokemon.data.model.pokemon.Type
import com.gaurav.pokemon.data.remote.ResponseHandler
import com.gaurav.pokemon.databinding.FragmentPokemonDetails2Binding
import com.gaurav.pokemon.ui.main.MainViewModel
import com.gaurav.pokemon.utils.*
import com.gaurav.pokemon.utils.Constants.POKEMON_CAPTURED
import com.gaurav.pokemon.utils.Constants.POKEMON_CAPTURED_BY_OTHER
import com.gaurav.pokemon.utils.Constants.POKEMON_DETAILS
import com.gaurav.pokemon.utils.Constants.POKEMON_WILD
import com.gaurav.pokemon.utils.GeneralUtils.parseDateToShortMonthDateAndYear
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber


class PokemonDetailsFragment : Fragment() {

    private val mainViewModel by sharedViewModel<MainViewModel>()
    private val gson: Gson by inject(Gson::class.java)

    private lateinit var googleMap: GoogleMap
    private lateinit var callback: OnMapReadyCallback
    private lateinit var pokemonDetails: PokemonDetails

    private var _binding: FragmentPokemonDetails2Binding? = null
    private val binding get() = _binding!!

    private var isWild = false
    private var isCaptured = false
    private var isCapturedByOther = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetails2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.llProgressBar.visibility = View.VISIBLE

        listener()

        requireActivity().intent?.extras?.let { it ->
            it.getSerializable(POKEMON_DETAILS)?.let {

                // Convert serializable to pokemon details object
                val type = object : TypeToken<PokemonDetails>() {}.type
                val pokemonDetailsString = gson.toJson(it, type)
                pokemonDetails = gson.fromJson(pokemonDetailsString, type)

                Timber.d("pokemonDetails : $pokemonDetails")

                val pokemonLocationInfo = pokemonDetails.pokemonLocationInfo

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

                setMarker(
                    LatLng(
                        pokemonLocationInfo.capturedLatAt,
                        pokemonLocationInfo.capturedLongAt
                    )
                )

                // Pokemon details api response handler
                lifecycleScope.launch {

                    // Set pokemon ID when routing from explore fragment
                    val pokemonId =
                        if (pokemonLocationInfo.id == 0) pokemonLocationInfo.name
                        else pokemonLocationInfo.id.toString()

                    mainViewModel.fetchPokemonDetails(pokemonId)
                        .let { apiResponse ->
                            when (apiResponse) {
                                is ResponseHandler.Success -> {

                                    apiResponse.data?.let { getPokemonResponse ->
                                        Timber.d("Pokemon details info $getPokemonResponse")
                                        setupUi(getPokemonResponse)
                                    }
                                }

                                is ResponseHandler.Error -> {
                                    Timber.e("Pokemon details error response: $apiResponse")
                                    handleApiError(apiResponse, requireActivity())
                                }

                                is ResponseHandler.Loading -> {
                                }
                            }
                        }
                }
            }
        }
    }

    private fun listener() {
        val scrollBounds = Rect()
        binding.scrollView.getHitRect(scrollBounds)

        binding.scrollView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (binding.ivPokemon.getLocalVisibleRect(scrollBounds)) {
                    if (!binding.ivPokemon.getLocalVisibleRect(scrollBounds)
                        || scrollBounds.height() < binding.ivPokemon.height
                    ) {
                        showToolbar()
                    } else {
                        hideToolbar()
                    }
                } else {
                    hideToolbar()
                }
            })
    }

    private fun showToolbar() {
        binding.ivPokemon.visibility = View.INVISIBLE
        binding.llToolbar.visibility = View.VISIBLE
        binding.tvPokemonName.visibility = View.INVISIBLE
        binding.fabCapture.visibility = View.INVISIBLE
    }

    private fun hideToolbar() {
        if (!isWild && !isCapturedByOther) {
            binding.fabCapture.visibility = View.VISIBLE
        }
        binding.ivPokemon.visibility = View.VISIBLE
        binding.llToolbar.visibility = View.GONE
        binding.tvPokemonName.visibility = View.VISIBLE
    }

    private fun startAnimation() {
        binding.clPokeballStatus.visibility = View.VISIBLE

        val clkRotate = AnimationUtils.loadAnimation(
            requireActivity(),
            R.anim.anim_rotate_clockwise
        )

        binding.clPokeballStatus.startAnimation(clkRotate)
    }

    private fun stopAnimation(isSuccess: Boolean) {
        binding.clPokeballStatus.clearAnimation()
        Handler(Looper.getMainLooper()).postDelayed(
            {
                binding.clPokeballStatus.visibility =
                    View.GONE
                if (isSuccess) {
                    binding.fabCapture.visibility =
                        View.VISIBLE
                } else {
                    binding.fabCapture.visibility =
                        View.GONE
                }
            },
            1000
        )
    }

    private fun setupUi(pokemon: Pokemon) {
        Timber.d("setupUi $pokemon")
        val captureButton: MaterialButton = binding.btnCapture
        val tvCaptureDate = binding.layoutBasicInfo.tvCaptureDate
        val captureLayout = binding.layoutBasicInfo.tvCaptureDate
        val tvFoundCaptured: TextView = binding.layoutMap.tvFoundInCapturedIn
        val pokemonCaptured = binding.fabCapture
        val layoutMap = binding.layoutMap.cvFoundCaptured
        val layoutCapturedBy = binding.layoutCapturedBy.cvCapturedBy
        val tvPokemon = binding.tvPokemonName

        val tvCapturedBy = binding.layoutCapturedBy.tvName
        val tvLevel = binding.layoutBasicInfo.tvLevel

        setupRecyclerView(pokemon.types)

        requireActivity().getDominantColor(pokemon.sprites.frontDefault, binding.ivPokemon) {

            binding.clMain.setBackgroundColor(it)
            binding.scrollView.setBackgroundColor(it)
            binding.llToolbar.setBackgroundColor(it)

            /*val gd = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(-0x9e9d9f, it)
            )
            gd.cornerRadius = 0f

            binding.clMain.background = gd
            binding.scrollView.background = (gd)*/
        }

        tvPokemon.text = pokemon.name.make1stCharacterUpper()
        binding.tvToolbarName.text = pokemon.name.make1stCharacterUpper()
        tvLevel.text = pokemon.id.toString()
        tvCapturedBy.text = pokemonDetails.capturedByName
        tvCaptureDate.text =
            parseDateToShortMonthDateAndYear(pokemonDetails.pokemonLocationInfo.capturedAt)

        if (isWild) {
            captureLayout.visibility = View.GONE
            pokemonCaptured.visibility = View.GONE
            captureButton.visibility = View.VISIBLE
            binding.layoutBasicInfo.tvCaptureOn.visibility = View.GONE
        } else {
            captureLayout.visibility = View.VISIBLE
            pokemonCaptured.visibility = View.VISIBLE
            binding.ivPokemonToolbar.visibility = View.VISIBLE
            captureButton.visibility = View.GONE
            tvFoundCaptured.text = getString(R.string.captured_in)
        }

        if (isCapturedByOther) {

            pokemonCaptured.visibility = View.GONE
            captureButton.visibility = View.GONE
            tvFoundCaptured.text = getString(R.string.captured_in)
            layoutMap.visibility = View.GONE
            layoutCapturedBy.visibility = View.VISIBLE
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
                            .fromResource(R.drawable.ic_pokeball_96)
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

        saveButton.setOnClickListener {
            if (etName.editableText.isNullOrEmpty()) {
                requireActivity().showToast("Please assign a name!")
            } else {
                startAnimation()
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

                lifecycleScope.launch {
                    mainViewModel.capturePokemonData(capturePokemon).let { apiResponse ->
                        when (apiResponse) {
                            is ResponseHandler.Success -> {

                                apiResponse.data?.let { captureResponse ->
                                    Timber.d("Pokemon captured : $captureResponse")
                                    stopAnimation(captureResponse.successful)
                                }
                            }

                            is ResponseHandler.Error -> {
                                Timber.e("capturePokemonData error response: $apiResponse")
                                handleApiError(apiResponse, requireActivity())
                            }

                            is ResponseHandler.Loading -> {
                            }
                        }
                    }
                }
            }

            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
    }
}