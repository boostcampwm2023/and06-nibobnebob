package com.avengers.nibobnebob.presentation.ui.main.mypage.wishlist

import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentWishRestaurantListBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.ui.main.mypage.share.MyPageSharedUiEvent
import com.avengers.nibobnebob.presentation.ui.main.mypage.share.MyPageSharedViewModel
import com.avengers.nibobnebob.presentation.ui.toAddRestaurant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishRestaurantListFragment :
    BaseFragment<FragmentWishRestaurantListBinding>(R.layout.fragment_wish_restaurant_list) {

    override val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: MyWishListViewModel by viewModels()
    private val sharedViewModel: MyPageSharedViewModel by viewModels()
    private val adapter =
        MyWishAdapter({ id -> showDeleteCheckDialog(id) }, { id -> viewModel.showDetail(id) },
            { item -> viewModel.addMyList(item) })


    override fun initView() = with(binding) {
        svm = sharedViewModel
        vm = viewModel

        rvWishRestaurant.adapter = adapter
        rvWishRestaurant.animation = null
        rvWishRestaurant.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val scrollBottom = !rvWishRestaurant.canScrollVertically(1)
                val hasNextPage = viewModel.uiState.value.lastPage
                val isNotLoading = !viewModel.uiState.value.isLoading

                if (scrollBottom && hasNextPage && isNotLoading) {
                    viewModel.loadNextPage()
                }
            }
        })
        setFilterMenu()
    }

    override fun initNetworkView() {
        viewModel.myWishList()
    }

    override fun initEventObserver() {
        repeatOnStarted {
            sharedViewModel.uiEvent.collect { event ->
                when (event) {
                    is MyPageSharedUiEvent.NavigateToBack ->
                        findNavController().navigate(WishRestaurantListFragmentDirections.globalToMyPageFragment())

                    else -> Unit
                }

            }
        }

        repeatOnStarted {
            viewModel.events.collect { event ->
                when (event) {
                    is MyWishEvent.NavigateToRestaurantDetail -> findNavController().toRestaurantDetail(
                        event.id
                    )

                    is MyWishEvent.NavigateToRestaurantAdd -> findNavController().toAddRestaurant(
                        event.item.name,
                        event.item.id
                    )

                    is MyWishEvent.ShowToastMessage -> showToastMessage(event.msg)
                    is MyWishEvent.ShowSnackMessage -> showSnackBar(event.msg)
                }
            }
        }
    }


    private fun showDeleteCheckDialog(id: Int) {
        showTwoButtonTitleDialog(
            title = "삭제하시겠습니까?",
            description = "내 위시 맛집 리스트에서 삭제됩니다.",
            confirmBtnClickListener = {
                viewModel.deleteWishList(id)
            }
        )
    }

    private fun setFilterMenu() {

        binding.tvFilter.setOnClickListener {
            PopupMenu(requireContext(), binding.ivFilter).apply {
                menuInflater.inflate(R.menu.my_page_filter_menu, menu)
                setOnMenuItemClickListener {
                    adapter.submitList(emptyList())
                    viewModel.myWishList(
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

    private fun NavController.toRestaurantDetail(restaurantId: Int) {
        val action =
            WishRestaurantListFragmentDirections.actionWishRestaurantListFragmentToRestaurantDetailFragment(
                restaurantId
            )
        navigate(action)
    }

}