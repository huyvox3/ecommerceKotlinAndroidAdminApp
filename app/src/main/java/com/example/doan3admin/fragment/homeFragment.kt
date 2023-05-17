package com.example.doan3admin.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.doan3admin.R
import com.example.doan3admin.activity.allOrdersActivity
import com.example.doan3admin.databinding.FragmentHomeBinding


class homeFragment : Fragment() {
    // TODO: Rename and change types of parameters


    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentHomeBinding.inflate(layoutInflater)


        binding.addCategoryBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_categoryFragment)
        }
        binding.addProductsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_productsFragment)
        }
        binding.addSliderBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_sliderFragment)
        }
        binding.allOrderBtn.setOnClickListener {
            startActivity(Intent(requireContext(),allOrdersActivity::class.java))
        }

        return  binding.root
    }


}
