package com.avengers.nibobnebob.presentation.ui.main.mypage.mylist

import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentMyRestaurantListBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.ui.main.mypage.share.MyPageSharedUiEvent
import com.avengers.nibobnebob.presentation.ui.main.mypage.share.MyPageSharedViewModel
import com.avengers.nibobnebob.presentation.ui.toMyPage
import com.avengers.nibobnebob.presentation.ui.toRestaurantDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyRestaurantListFragment :
    BaseFragment<FragmentMyRestaurantListBinding>(R.layout.fragment_my_restaurant_list) {

    private val sharedViewModel: MyPageSharedViewModel by viewModels()
    private val viewModel: MyRestaurantListViewModel by viewModels()
    override val parentViewModel: MainViewModel by activityViewModels()
    private val adapter = MyRestaurantAdapter({ id -> viewModel.showDetail(id) },
        { id -> showDeleteCheckDialog(id) })

    override fun initView() = with(binding) {
        svm = sharedViewModel
        vm = viewModel

        rvMyRestaurant.adapter = adapter
        rvMyRestaurant.animation = null
        rvMyRestaurant.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!rvMyRestaurant.canScrollVertically(1) && viewModel.uiState.value.lastPage) {
                    viewModel.loadNextPage()
                }
            }
        })
        setFilterMenu()
    }

    override fun initNetworkView() {
        viewModel.myRestaurantList()
    }

    override fun initEventObserver() {
        repeatOnStarted {
            sharedViewModel.uiEvent.collect { event ->
                when (event) {
                    is MyPageSharedUiEvent.NavigateToBack -> findNavController().toMyPage()
                    else -> Unit
                }

            }
        }

        repeatOnStarted {
            viewModel.events.collect { event ->
                when (event) {
                    is MyRestaurantEvent.NavigateToRestaurantDetail -> findNavController().toRestaurantDetail(
                        event.id
                    )

                    is MyRestaurantEvent.ShowToastMessage -> showToastMessage(event.msg)
                    is MyRestaurantEvent.ShowSnackMessage -> showSnackBar(event.msg)
                }
            }
        }
    }


    private fun showDeleteCheckDialog(id: Int) {
        showTwoButtonTitleDialog(
            title = "삭제하시겠습니까?",
            description = "내 맛집 리스트에서 삭제됩니다.",
            confirmBtnClickListener = {
                viewModel.deleteMyList(id)
            }
        )
    }

    private fun setFilterMenu() {

        binding.tvFilter.setOnClickListener {
            PopupMenu(requireContext(), binding.ivFilter).apply {
                menuInflater.inflate(R.menu.my_page_filter_menu, menu)
                setOnMenuItemClickListener {
                    adapter.submitList(emptyList())
                    viewModel.myRestaurantList(
                        sort = when (it.itemId) {
                            R.id.menu_new -> "TIME_DESC"
                            R.id.menu_old -> "TIME_ASC"
                            else -> null
                        }
                    )
                    true
                }
                show()
            }
        }

    }


}