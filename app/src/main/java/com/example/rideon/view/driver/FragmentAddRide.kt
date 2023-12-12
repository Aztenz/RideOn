package com.example.rideon.view.driver

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioButton
import com.example.rideon.R
import java.util.Calendar
import java.util.concurrent.TimeUnit

class FragmentAddRide : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(
            R.layout.fragment_add_ride,
            container, false)


        val pickupEdit: EditText = view.findViewById(R.id.edit_pickup_fragment_add_ride)
        val dropOffEdit: EditText = view.findViewById(R.id.edit_drop_off_fragment_ar)
        val pickupCheckBox: CheckBox = view.findViewById(R.id.check_pickup_gates_fragment_ar)
        val dropOffCheckBox: CheckBox = view.findViewById(R.id.check_drop_off_gates_fragment_ar)
        val gate3Pickup: RadioButton = view.findViewById(R.id.radio_gate_3_pickup_fragment_ar)
        val gate4Pickup: RadioButton = view.findViewById(R.id.radio_gate_4_pickup_fragment_ar)
        val gate3DropOff: RadioButton = view.findViewById(R.id.radio_gate_3_drop_off_fragment_ar)
        val gate4DropOff: RadioButton = view.findViewById(R.id.radio_gate_4_drop_off_fragment_ar)

        val datePicker: DatePicker = view.findViewById(R.id.date_picker)
        val morningTime: RadioButton = view.findViewById(R.id.radio_morning_time)
        val eveningTime: RadioButton = view.findViewById(R.id.radio_evening_time)
        val confirmBtn: Button = view.findViewById(R.id.button_confirm)


        val currentTimeMillis = System.currentTimeMillis()
        datePicker.minDate = currentTimeMillis
        val currentCalendar = Calendar.getInstance().apply {
            timeInMillis = currentTimeMillis
        }

        //TODO: add new button to validate
        confirmBtn.setOnClickListener {
            val pickupTime: String? =
                getTime(
                    pickupEdit,
                    gate3Pickup,
                    gate4Pickup)

            val dropOffTime: String? =
                getTime(
                    dropOffEdit,
                    gate3DropOff,
                    gate4DropOff)
            if(pickupTime.isNullOrBlank() || dropOffTime.isNullOrBlank()) {
                Log.d("myapp101", "invalid locations")
                return@setOnClickListener
            }

            val selectedCalendar: Calendar
            val hoursToCompare : Int
            if(morningTime.isChecked) {
                selectedCalendar = Calendar.getInstance().apply {
                    set(datePicker.year,
                        datePicker.month,
                        datePicker.dayOfMonth,
                        7,
                        30)
                }
                hoursToCompare = 9
            } else if(eveningTime.isChecked) {
                selectedCalendar = Calendar.getInstance().apply {
                    set(datePicker.year,
                        datePicker.month,
                        datePicker.dayOfMonth,
                        17,
                        30)
                }
                hoursToCompare = 4
            } else {
                Log.d("myapp101", "invalid")
                return@setOnClickListener
            }


            val timeDifferenceInMillis =
                selectedCalendar.timeInMillis - currentCalendar.timeInMillis

            val hours = TimeUnit.MILLISECONDS
                .toHours(timeDifferenceInMillis)
            val minutes = TimeUnit.MILLISECONDS
                .toMinutes(timeDifferenceInMillis)%
                    TimeUnit.HOURS.toMinutes(1)

            val isRideTimeValid = hours > hoursToCompare || (hours.toInt() == hoursToCompare && minutes >= 30)
            if(isRideTimeValid){
                Log.d("myapp101", "Success")
            }
        }

        pickupCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){

                pickupEdit.isEnabled = false
                dropOffCheckBox.isChecked = false

                gate3Pickup.isEnabled = true
                gate4Pickup.isEnabled = true
            } else {
                pickupEdit.isEnabled = true
                dropOffCheckBox.isChecked = true

                gate3Pickup.isEnabled = false
                gate4Pickup.isEnabled = false
                gate3Pickup.isChecked = false
                gate4Pickup.isChecked = false
            }
        }

        dropOffCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                dropOffEdit.isEnabled = false
                pickupCheckBox.isChecked = false

                gate3DropOff.isEnabled = true
                gate4DropOff.isEnabled = true
            } else {
                dropOffEdit.isEnabled = true
                pickupCheckBox.isChecked = true

                gate3DropOff.isEnabled = false
                gate4DropOff.isEnabled = false
                gate3DropOff.isChecked = false
                gate4DropOff.isChecked = false
            }
        }

        return view
    }

    private fun getTime(edit: EditText,
                        radio1: RadioButton,
                        radio2: RadioButton): String? {
        return when {
            radio1.isChecked -> "Gate 3"
            radio2.isChecked -> "Gate 4"
            edit.text.isNotEmpty() -> edit.text.toString()
            edit.text.toString().contains("\\b\\s*gate\\s*3\\s*\\b\n") -> null
            edit.text.toString() == "\\b\\s*gate\\s*4\\s*\\b\n" -> null
            else -> null
        }
    }

}