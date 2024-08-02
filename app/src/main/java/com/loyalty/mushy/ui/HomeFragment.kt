package com.loyalty.mushy.ui

import android.content.res.AssetManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.loyalty.mushy.R
import com.loyalty.mushy.databinding.FragmentHomeBinding
import com.loyalty.mushy.viewmodel.MainViewModel
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class HomeFragment : Fragment() {
   lateinit var binding: FragmentHomeBinding
   var inputsArray = Array(11){""}
   private lateinit var interpreter: Interpreter
   private val mModelPath = "converted_model.tflite"

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View? {
      // Inflate the layout for this fragment
      return inflater.inflate(R.layout.fragment_home, container, false)
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      binding = FragmentHomeBinding.bind(view)
      initInterpreter()
      val capSurface = ArrayAdapter(requireContext(), R.layout.questions, resources.getStringArray(R.array.cap_surface))
      val capShape = ArrayAdapter(requireContext(), R.layout.questions, resources.getStringArray(R.array.cap_shape))
      val capColor = ArrayAdapter(requireContext(), R.layout.questions, resources.getStringArray(R.array.cap_color))
      val bruises = ArrayAdapter(requireContext(), R.layout.questions, resources.getStringArray(R.array.bruises))
      val sporePrintColor = ArrayAdapter(requireContext(), R.layout.questions, resources.getStringArray(R.array.spore_print_color))
      val gillSize = ArrayAdapter(requireContext(), R.layout.questions, resources.getStringArray(R.array.gill_size))
      val habitat = ArrayAdapter(requireContext(), R.layout.questions, resources.getStringArray(R.array.habitat))
      val population = ArrayAdapter(requireContext(), R.layout.questions, resources.getStringArray(R.array.population))
      val ringType = ArrayAdapter(requireContext(), R.layout.questions, resources.getStringArray(R.array.ring_type))
      val gillColor = ArrayAdapter(requireContext(), R.layout.questions, resources.getStringArray(R.array.gill_color))
      val ringNumber = ArrayAdapter(requireContext(), R.layout.questions, resources.getStringArray(R.array.ring_number))

      binding.ac1.setAdapter(gillColor)
      binding.ac2.setAdapter(ringNumber)
      binding.ac3.setAdapter(ringType)
      binding.ac4.setAdapter(population)
      binding.ac5.setAdapter(habitat)
      binding.ac6.setAdapter(bruises)
      binding.ac7.setAdapter(gillSize)
      binding.ac8.setAdapter(sporePrintColor)
      binding.ac9.setAdapter(capShape)
      binding.ac10.setAdapter(capSurface)
      binding.ac11.setAdapter(capColor)

      autoListener(binding.ac1, 0)
      autoListener(binding.ac2, 1)
      autoListener(binding.ac3, 2)
      autoListener(binding.ac4, 3)
      autoListener(binding.ac5, 4)
      autoListener(binding.ac6, 5)
      autoListener(binding.ac7, 6)
      autoListener(binding.ac8, 7)
      autoListener(binding.ac9, 8)
      autoListener(binding.ac10, 9)
      autoListener(binding.ac11, 10)

      val mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

      binding.predictBtn.setOnClickListener {
         if(inputsArray.contains("")){
            Toast.makeText(requireContext(), "Please fill all the questions", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
         }else {
            mainViewModel.convertValues(inputsArray, interpreter)
            mainViewModel.hasil.observe(viewLifecycleOwner) { hasil ->
               if (hasil > 0.5) {
                  MaterialAlertDialogBuilder(requireContext(), com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered)
                     .setTitle("Result!")
                     .setMessage("It's a poisonous mushroom! You shouldn't eat it")
                     .setNegativeButton("Close") { dialog, which ->
                        dialog.cancel()
                        resetAnswers()
                     }
                     .setIcon(resources.getDrawable(R.drawable.baseline_dangerous_24))
                     .show()
               } else {
                  MaterialAlertDialogBuilder(requireContext(), com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered)
                     .setTitle("Result!")
                     .setMessage("This mushroom is safe to be eaten, make sure to cook it properly")
                     .setNegativeButton("Close") { dialog, which ->
                        dialog.cancel()
                        resetAnswers()
                     }
                     .setIcon(resources.getDrawable(R.drawable.baseline_check_circle_outline_24))
                     .show()
               }
            }
         }
      }

      binding.clearBtn.setOnClickListener {
         resetAnswers()
         Toast.makeText(requireContext(), "Cleared", Toast.LENGTH_SHORT).show()
      }
   }

   private fun initInterpreter() {
      val options = Interpreter.Options()
      options.setNumThreads(4)
      options.setUseNNAPI(true)
      interpreter = Interpreter(loadModelFile(context?.assets!!, mModelPath), options)

   }

   fun resetAnswers(){
      with(binding) {
         ac1.setText("")
         ac2.setText("")
         ac3.setText("")
         ac4.setText("")
         ac5.setText("")
         ac6.setText("")
         ac7.setText("")
         ac8.setText("")
         ac9.setText("")
         ac10.setText("")
         ac11.setText("")
      }
      inputsArray = Array(11){""}
   }

   private fun loadModelFile(assets: AssetManager, mModelPath: String): MappedByteBuffer {
      val fileDescriptor = assets.openFd(mModelPath)
      val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
      val fileChannel = inputStream.channel
      val startOffset = fileDescriptor.startOffset
      val declaredLength = fileDescriptor.declaredLength
      return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
   }


   private fun autoListener(ac: AutoCompleteTextView, index: Int) {
      ac.setOnItemClickListener { _, _, position, _ ->
         inputsArray[index] = ac.adapter.getItem(position).toString()
      }
   }

}