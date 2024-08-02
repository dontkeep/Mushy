package com.loyalty.mushy.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.loyalty.mushy.R
import com.loyalty.mushy.databinding.FragmentDatasetBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class DatasetFragment : Fragment() {
   private lateinit var binding: FragmentDatasetBinding
   var tableData: List<List<String>> = emptyList()
   private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())


   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View? {
      // Inflate the layout for this fragment
      return inflater.inflate(R.layout.fragment_dataset, container, false)
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      binding = FragmentDatasetBinding.bind(view)

      coroutineScope.launch {
         tableData =loadCSVData()
         Log.d("Dataset Fragment : ", "Data loaded : $tableData")
         displayTableData(tableData)
      }
   }

   private suspend fun loadCSVData():List<List<String>> = withContext(Dispatchers.IO) {
      val tableDataList = mutableListOf<List<String>>()
      try {
         resources.openRawResource(R.raw.mushrooms).use { inputStream ->
            val reader = inputStream.bufferedReader()
            var line: String? = reader.readLine()
            while (line != null) {
               val rowData = line.split(",")
               tableDataList.add(rowData)
               line = reader.readLine()
            }
         }
      } catch (e: IOException) {
         Log.e("Dataset Fragment : ", "Error loading CSV data", e)
      }

      tableDataList
   }

   private fun displayTableData(tableData: List<List<String>>) {
      binding.tableLayout.removeAllViews()

      val maxRows = 15
      val rowsToShow = tableData.take(maxRows)

      for (rowData in rowsToShow) {
         val tableRow = TableRow(requireContext())
         val layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.WRAP_CONTENT,
            TableLayout.LayoutParams.WRAP_CONTENT
         )
         tableRow.layoutParams = layoutParams
         tableRow.setPadding(0, 0, 0, 0)

         for (column in rowData) {
            val textView = TextView(requireContext())
            textView.text = column
            textView.setPadding(8, 8, 8, 8)
            textView.setBackgroundResource(R.drawable.cell_border)
            tableRow.addView(textView)
         }

         binding.tableLayout.addView(tableRow)
      }
   }

   override fun onDestroy() {
      super.onDestroy()
      coroutineScope.cancel() // Cancel any running coroutines when the Fragment is destroyed
   }

   companion object {
   }
}