package com.fs.stockmanagementapp.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fs.stockmanagementapp.R
import com.fs.stockmanagementapp.data.model.Product
import com.fs.stockmanagementapp.databinding.FragmentDashboardBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.lowStockProducts.observe(viewLifecycleOwner) { lowStockList ->
            binding.cardLowStock.isVisible = lowStockList.isNotEmpty()
            if (lowStockList.isNotEmpty()) {
                binding.textViewLowStockInfo.text = "${lowStockList.size} product stock is at a critical level."
            }
        }

        viewModel.topStockProducts.observe(viewLifecycleOwner) { products ->
            if (products.isNotEmpty()) {
                binding.barChart.isVisible = true
                binding.textViewChartTitle.isVisible = true
                setupBarChart(products)
            } else {
                binding.barChart.isVisible = false
                binding.textViewChartTitle.isVisible = false
            }
        }

        binding.buttonManageProducts.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_productListFragment)
        }
        binding.buttonManageSuppliers.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_supplierListFragment)
        }
        binding.buttonViewTransactions.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_transactionHistoryFragment)
        }
    }

    private fun setupBarChart(products: List<Product>) {
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()

        products.forEachIndexed { index, product ->
            entries.add(BarEntry(index.toFloat(), product.currentStockLevel.toFloat()))
            labels.add(product.name)
        }


        val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true)
        val primaryColor = typedValue.data


        val dataSet = BarDataSet(entries, "Stock Quantity")
        dataSet.color = primaryColor
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        val barData = BarData(dataSet)
        binding.barChart.data = barData

        val xAxis = binding.barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true

        binding.barChart.axisRight.isEnabled = false
        binding.barChart.description.isEnabled = false
        binding.barChart.animateY(1000)
        binding.barChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}