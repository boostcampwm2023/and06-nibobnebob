package com.avengers.nibobnebob.presentation.ui.main.global.restaurantadd

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.customBack
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.ui.toHome
import com.avengers.nibobnebob.presentation.ui.toMultiPart
import com.avengers.presentation.R
import com.avengers.presentation.databinding.FragmentAddMyRestaurantBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddMyRestaurantFragment :
    BaseFragment<FragmentAddMyRestaurantBinding>(R.layout.fragment_add_my_restaurant) {

    override val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: AddMyRestaurantViewModel by viewModels()

    private val args: AddMyRestaurantFragmentArgs by navArgs()
    private val restaurantName by lazy { args.restaurantName }
    private val restaurantId by lazy { args.restaurantId }

    override fun initView() {
        binding.vm = viewModel
        viewModel.setDefaultValue(restaurantName, restaurantId)
        setSliderListener()
        setVisitMethodRadioListener()
        initImageObserver()
        customBack(requireActivity(), findNavController())
    }

    override fun initNetworkView() {
        //TODO : 네트워크
    }

    override fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is AddMyRestaurantEvents.NavigateToBack -> findNavController().navigateUp()
                    is AddMyRestaurantEvents.ShowConfirmDialog -> {
                        showTwoButtonTitleDialog(
                            "리뷰를 등록하시겠습니까?",
                            "리뷰는 수정/삭제가 불가능합니다!",
                        ) {
                            viewModel.addReview()
                        }
                    }

                    is AddMyRestaurantEvents.ShowSuccessDialog -> {
                        showOneButtonTitleDialog("리뷰 등록을 완료했습니다!") {
                            findNavController().toHome(restaurantId)
                        }
                    }

                    is AddMyRestaurantEvents.ShowSnackMessage -> showSnackBar(it.msg)
                    is AddMyRestaurantEvents.ShowToastMessage -> showToastMessage(it.msg)
                    is AddMyRestaurantEvents.OpenGallery -> parentViewModel.openGallery()
                    is AddMyRestaurantEvents.ShowLoading -> showLoading(requireContext())
                    is AddMyRestaurantEvents.DismissLoading -> dismissLoading()
                }
            }
        }
    }


    private fun setSliderListener() {
        with(binding) {
            sdParkingSpace.addOnChangeListener { _, value, _ ->
                viewModel.sliderStateChange(EstimateItem.PARKING, value.toInt())
            }

            sdTraffic.addOnChangeListener { _, value, _ ->
                viewModel.sliderStateChange(EstimateItem.TRAFFIC, value.toInt())
            }

            sdService.addOnChangeListener { _, value, _ ->
                viewModel.sliderStateChange(EstimateItem.SERVICE, value.toInt())
            }

            sdTaste.addOnChangeListener { _, value, _ ->
                viewModel.sliderStateChange(EstimateItem.TASTE, value.toInt())
            }

            sdToilet.addOnChangeListener { _, value, _ ->
                viewModel.sliderStateChange(EstimateItem.TOILET, value.toInt())
            }
        }
    }

    private fun setVisitMethodRadioListener() {
        binding.rgVisitMethod.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_visit_not_car -> viewModel.setIsVisitWithCar(false)
                R.id.rb_visit_car -> viewModel.setIsVisitWithCar(true)
            }
        }
    }

    private fun initImageObserver(){
        repeatOnStarted {
            parentViewModel.image.collect{
                if(it.isNotBlank()){
                    viewModel.setImage(it, it.toMultiPart(requireContext(),"reviewImage"))
                    parentViewModel.uriCollected()
                }
            }
        }
    }
}