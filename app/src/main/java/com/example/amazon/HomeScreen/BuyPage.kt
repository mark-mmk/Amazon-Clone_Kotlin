package com.example.amazon.HomeScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.amazon.Adapter.CartAdapter
import com.example.amazon.R
import com.example.amazon.databinding.FragmentBuyPageBinding
import com.example.amazon.test.carttest
class BuyPage : Fragment() {
    private var _binding: FragmentBuyPageBinding? = null
    private val binding get() = _binding!!
    lateinit var cartAdapter: CartAdapter
    lateinit var itemlist:ArrayList<carttest>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemlist= ArrayList()
        itemlist.add(carttest("1","2", R.drawable.icon,"mark mekhail","100","8",2000))
        itemlist.add(carttest("1","2", R.drawable.icon,"mark mekhail","100","8",2000))
        itemlist.add(carttest("1","2", R.drawable.icon,"mark mekhail","100","8",2000))
        itemlist.add(carttest("1","2", R.drawable.icon,"mark mekhail","100","8",2000))
        itemlist.add(carttest("1","2", R.drawable.icon,"mark mekhail","100","8",2000))
        itemlist.add(carttest("1","2", R.drawable.icon,"mark mekhail","100","8",2000))
        binding.recyclerItems.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        binding.recyclerItems.adapter = CartAdapter(itemlist)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBuyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}