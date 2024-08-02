package com.loyalty.mushy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.tensorflow.lite.Interpreter

class MainViewModel: ViewModel() {
   private val _hasil = MutableLiveData<Float>()
   val hasil: LiveData<Float> = _hasil

   //Unique values after LE in gill-color: [ 4 = black, 5 = brown, 2 = gray, 7 = pink, 10 = white, 3 = chocolate, 9 = purple, 1 = red, 0 = buff, 8 = green, 11 = yellow, 6 = orange]
   val gillColorValues = mapOf("Black" to 4, "Brown" to 5, "Gray" to 2, "Pink" to 7, "White" to 10, "Chocolate" to 3, "Purple" to 9, "Red" to 1, "Buff" to 0, "Green" to 8, "Yellow" to 11, "Orange" to 6)

   //Unique values after LE in cap-surface: [2 = smooth, 3 = scaly, 0 = fibrous, 1 = grooves]
   val capSurfaceValues = mapOf("Smooth" to 2, "Scaly" to 3, "Fibrous" to 0, "Grooves" to 1)

   //Unique values after LE in cap-shape: [5 = convex, 0 = bell, 4 = sunken, 2 = flat, 3 = knobbed, 1 = conical]
   val capShapeValues = mapOf("Convex" to 5, "Bell" to 0, "Sunken" to 4, "Flat" to 2, "Knobbed" to 3, "Conical" to 1)

   //Unique values after LE in cap-color: [4 = brown, 9 = yellow, 8 = white, 3 = gray, 2 = red, 5 = pink, 0 = buff, 7 = purple, 1 = cinnamon, 6 = green]
   val capColorValues = mapOf("Brown" to 4, "Yellow" to 9, "White" to 8, "Gray" to 3, "Red" to 2, "Pink" to 5, "Buff" to 0, "Purple" to 7, "Cinnamon" to 1, "Green" to 6)

   //Unique values after LE in bruises: [1 = true, 0 = false]
   val bruisesValues = mapOf("Yes" to 1, "No" to 0)

   //Unique values after LE in spore-print-color: [2 = black, 3 = brown, 6 = purple, 1 = chocolate, 7 = white, 5 = green, 4 = orange, 8 = yellow, 0 = buff]
   val sporePrintColorValues = mapOf("Black" to 2, "Brown" to 3, "Purple" to 6, "Chocolate" to 1, "White" to 7, "Green" to 5, "Orange" to 4, "Yellow" to 8, "Buff" to 0)

   //Unique values after LE in gill-size: [1 = narrow, 0 = broad]
   val gillSizeValues = mapOf("Narrow" to 1, "Broad" to 0)

   //Unique values after LE in habitat: [5 = urban, 1 = grasses, 3 = meadows, 0 = woods, 4 = paths, 6 = waste, 2 = leaves]
   val habitatValues = mapOf("Urban" to 5, "Grasses" to 1, "Meadows" to 3, "Wood" to 0, "Paths" to 4, "Waste" to 6, "Leaves" to 2)

   //Unique values after LE in population: [3 = scattered, 2 = numerous, 0 = abundant, 4 = several, 5 = solitary, 1 = clustered]
   val populationValues = mapOf("Scattered" to 3, "Numerous" to 2, "Abundant" to 0, "Several" to 4, "Solitary" to 5, "Clustered" to 1)

   //Unique values after LE in ring-type: [4 = pendant, 0 = evanescent, 2 = large, 1 = flaring,  3 = none]
   val ringTypeValues = mapOf("Pendant" to 4, "Evanescent" to 0, "Large" to 2, "Flaring" to 1, "None" to 3)

   //Unique values after LE in ring-number: [1 = one, 2 = two, 0 = none]
   val ringNumberValues = mapOf("One" to 1, "Two" to 2, "None" to 0)

   fun doInference(inputs: IntArray, interpreter: Interpreter){
      val input = FloatArray(11)
      if (inputs.size == 11){
         for (i in 0..10) {
            input[i] = inputs[i].toFloat()
         }
         val output = Array(1) { FloatArray(1) }
         interpreter.run(input, output)
         _hasil.value = output[0][0]
      }
   }

   fun reset(){
      _hasil.value = 0.0f
   }



   fun convertValues(inputs: Array<String>, interpreter: Interpreter){
      var finalInputs = IntArray(11)
      //inputs in order gill-color, ring-number, ring-type, population, habitat, bruises, gill-size, spore-print-color, cap-shape, cap-surface, cap-color
      finalInputs[0] = gillColorValues[inputs[0]]!!
      finalInputs[1] = ringNumberValues[inputs[1]]!!
      finalInputs[2] = ringTypeValues[inputs[2]]!!
      finalInputs[3] = populationValues[inputs[3]]!!
      finalInputs[4] = habitatValues[inputs[4]]!!
      finalInputs[5] = bruisesValues[inputs[5]]!!
      finalInputs[6] = gillSizeValues[inputs[6]]!!
      finalInputs[7] = sporePrintColorValues[inputs[7]]!!
      finalInputs[8] = capShapeValues[inputs[8]]!!
      finalInputs[9] = capSurfaceValues[inputs[9]]!!
      finalInputs[10] = capColorValues[inputs[10]]!!

      doInference(finalInputs, interpreter)
   }

}