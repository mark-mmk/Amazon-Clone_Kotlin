package com.example.amazon.HomeScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.amazon.Adapter.CartAdapter
import com.example.amazon.Adapter.CategoryAdapter
import com.example.amazon.Adapter.CategoryItem
import com.example.amazon.MainActivity
import com.example.amazon.R

import com.example.amazon.databinding.FragmentHomePageBinding
import com.example.amazon.test.carttest
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomePage : Fragment()
    ,SwipeRefreshLayout.OnRefreshListener
{
    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!
    lateinit var itemlist:ArrayList<CategoryItem>
    lateinit var itemlist2:ArrayList<carttest>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swiperefresh.setOnRefreshListener(this)
        itemlist= ArrayList()
        itemlist.add(CategoryItem("phone"))
        itemlist.add(CategoryItem("Laptop"))
        itemlist.add(CategoryItem("Lcd"))
        itemlist.add(CategoryItem("AirBods"))
        itemlist.add(CategoryItem("Computers"))
        binding.recyclerCategory.layoutManager= StaggeredGridLayoutManager(1, RecyclerView.HORIZONTAL)
        binding.recyclerCategory.adapter=CategoryAdapter(itemlist)

        itemlist2= ArrayList()
        itemlist2.add(carttest("1","2", R.drawable.icon,"mark mekhail","100","8",2000))
        itemlist2.add(carttest("1","2", R.drawable.icon,"mark mekhail","100","8",2000))
        itemlist2.add(carttest("1","2", R.drawable.icon,"mark mekhail","100","8",2000))
        itemlist2.add(carttest("1","2", R.drawable.icon,"mark mekhail","100","8",2000))
        itemlist2.add(carttest("1","2", R.drawable.icon,"mark mekhail","100","8",2000))
        itemlist2.add(carttest("1","2", R.drawable.icon,"mark mekhail","100","8",2000))
        binding.recyclerProducts.layoutManager =
            StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        binding.recyclerProducts.adapter = CartAdapter(itemlist2)

    }
    override fun onRefresh() {
        Handler(Looper.getMainLooper()).postDelayed({
            var i = Intent(requireActivity(), MainActivity::class.java)
            startActivity(i)
            Toast.makeText(requireActivity(), "Refresh", Toast.LENGTH_LONG).show()
            binding.swiperefresh.isRefreshing = false
        }, 2000)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}