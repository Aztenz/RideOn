package com.example.rideon.controller.driver.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.rideon.R
import com.example.rideon.controller.driver.Config
import com.example.rideon.model.data_classes.Ride
import com.example.rideon.model.data_classes.User
import com.example.rideon.model.database.firebase.AccountManager
import com.example.rideon.model.database.firebase.DriverManager
import com.example.rideon.model.database.room.RoomAccountManager
import com.example.rideon.utilities.NetworkUtils
import com.google.firebase.Timestamp
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class AddRide : Fragment() {

    private lateinit var pickupEdit: EditText
    private lateinit var dropOffEdit: EditText
    private lateinit var priceEdit: EditText
    private lateinit var pickupCheckBox: CheckBox
    private lateinit var dropOffCheckBox: CheckBox
    private lateinit var gate3Pickup: RadioButton
    private lateinit var gate4Pickup: RadioButton
    private lateinit var gate3DropOff: RadioButton
    private lateinit var gate4DropOff: RadioButton
    private lateinit var datePicker: DatePicker
    private lateinit var morningTime: RadioButton
    private lateinit var eveningTime: RadioButton
    private lateinit var confirmBtn: Button
    private lateinit var motorcycleRadio: RadioButton
    private lateinit var carRadio: RadioButton
    private lateinit var vanRadio: RadioButton
    private lateinit var busRadio: RadioButton

    private val driverManager = DriverManager.instance
    private val roomAccountManager = RoomAccountManager.instance
    private lateinit var me: User
    private lateinit var networkUtil: NetworkUtils


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.d_fragment_add_ride,
            container, false)

        networkUtil = NetworkUtils.getInstance(requireContext())

        //get user
        roomAccountManager.getLoggedInUser(
            fragment = this@AddRide,
            onSuccess = { me = it },
            onFailure = { AccountManager
                .instance.logoutUser( onSuccess = {}, onFailure = {} ) } )

        initializeViews(view)
        setupDateTimePicker()

        // Observe network status
        networkUtil.isConnected.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                confirmBtn.isEnabled = true
            } else {
                confirmBtn.isEnabled = false
                Toast.makeText(requireActivity(),
                    Config.NO_INTERNET,
                    Toast.LENGTH_LONG).show()
            }
        }

        pickupCheckBox.setOnCheckedChangeListener { _, isChecked ->
            handlePickupCheckboxChange(isChecked)
        }

        dropOffCheckBox.setOnCheckedChangeListener { _, isChecked ->
            handleDropOffCheckboxChange(isChecked)
        }

        confirmBtn.setOnClickListener {
            handleConfirmButtonClick()
        }

        // Set default values
        pickupCheckBox.isChecked = true
        gate3Pickup.isChecked = true

        return view
    }

    private fun initializeViews(view: View) {
        pickupEdit = view.findViewById(R.id.edit_pickup_fragment_add_ride)
        dropOffEdit = view.findViewById(R.id.edit_drop_off_fragment_ar)
        priceEdit = view.findViewById(R.id.edit_price)
        pickupCheckBox = view.findViewById(R.id.check_pickup_gates_fragment_ar)
        dropOffCheckBox = view.findViewById(R.id.check_drop_off_gates_fragment_ar)
        gate3Pickup = view.findViewById(R.id.radio_gate_3_pickup_fragment_ar)
        gate4Pickup = view.findViewById(R.id.radio_gate_4_pickup_fragment_ar)
        gate3DropOff = view.findViewById(R.id.radio_gate_3_drop_off_fragment_ar)
        gate4DropOff = view.findViewById(R.id.radio_gate_4_drop_off_fragment_ar)
        datePicker = view.findViewById(R.id.date_picker)
        morningTime = view.findViewById(R.id.radio_morning_time)
        eveningTime = view.findViewById(R.id.radio_evening_time)
        confirmBtn = view.findViewById(R.id.button_confirm)
        motorcycleRadio = view.findViewById(R.id.radio_motorcycle)
        carRadio = view.findViewById(R.id.radio_car)
        vanRadio = view.findViewById(R.id.radio_van)
        busRadio = view.findViewById(R.id.radio_bus)
    }

    private fun setupDateTimePicker() {
        val currentTimeMillis = System.currentTimeMillis()
        datePicker.minDate = currentTimeMillis
    }

    private fun handlePickupCheckboxChange(isChecked: Boolean) {
        if (isChecked) {
            pickupEdit.isEnabled = false
            dropOffCheckBox.isChecked = false
            enablePickupRadioButtons()
        } else {
            pickupEdit.isEnabled = true
            dropOffCheckBox.isChecked = true
            disablePickupRadioButtons()
        }
    }

    private fun handleDropOffCheckboxChange(isChecked: Boolean) {
        if (isChecked) {
            dropOffEdit.isEnabled = false
            pickupCheckBox.isChecked = false
            enableDropOffRadioButtons()
        } else {
            dropOffEdit.isEnabled = true
            pickupCheckBox.isChecked = true
            disableDropOffRadioButtons()
        }
    }

    private fun handleConfirmButtonClick() {
        val type = getSelectedType()
        val pickupLocation = getLocation(pickupEdit, gate3Pickup, gate4Pickup)
        val dropOffLocation = getLocation(dropOffEdit, gate3DropOff, gate4DropOff)
        val priceText = priceEdit.text
        if (pickupLocation.isNullOrBlank() ||
            dropOffLocation.isNullOrBlank() ||
            priceText.isNullOrBlank()) {
            Log.d(Config.LOG_TAG, Config.MISSING_FIELDS)
            return
        }
        val isRideTimeValid = isRideTimeValid()
        if (isRideTimeValid) {
            driverManager.offerRide(
                Ride(
                    driverId = me.userId,
                    origin = pickupLocation.toString(),
                    destination = dropOffLocation.toString(),
                    availableSeats = type.second,
                    vehicleType = type.first,
                    date = Timestamp(getTime().time / 1000, 0).toDate(),
                    price = priceText.toString().toDouble(),
                    status = Config.RIDE_STATUS_AVAILABLE
                ),
                onSuccess = {
                    Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                },
                onFailure = {
                    Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun enablePickupRadioButtons() {
        gate3Pickup.isEnabled = true
        gate4Pickup.isEnabled = true
    }

    private fun disablePickupRadioButtons() {
        gate3Pickup.isEnabled = false
        gate4Pickup.isEnabled = false
        gate3Pickup.isChecked = false
        gate4Pickup.isChecked = false
    }

    private fun enableDropOffRadioButtons() {
        gate3DropOff.isEnabled = true
        gate4DropOff.isEnabled = true
    }

    private fun disableDropOffRadioButtons() {
        gate3DropOff.isEnabled = false
        gate4DropOff.isEnabled = false
        gate3DropOff.isChecked = false
        gate4DropOff.isChecked = false
    }

    private fun getSelectedType(): Pair<Int, Int> {
        return when {
            motorcycleRadio.isChecked -> Config.TYPE_MOTORCYCLE to Config.MOTORCYCLE_SEATS
            carRadio.isChecked -> Config.TYPE_CAR to Config.CAR_SEATS
            vanRadio.isChecked -> Config.TYPE_VAN to Config.VAN_SEATS
            busRadio.isChecked -> Config.TYPE_BUS to Config.BUS_SEATS
            else -> Config.TYPE_DEFAULT to Config.DEFAULT_SEATS
        }
    }

    private fun isRideTimeValid(): Boolean {
        val selectedCalendar = getTime().time

        val currentCalendar = Calendar.getInstance()
        val timeDifferenceInMillis =
            selectedCalendar - currentCalendar.timeInMillis

        val hours = TimeUnit.MILLISECONDS.toHours(timeDifferenceInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifferenceInMillis) %
                TimeUnit.HOURS.toMinutes(1)

        val hoursToCompare: Int = if (morningTime.isChecked) {
            Config.MORNING_HOURS_TO_COMPARE
        } else {
            Config.EVENING_HOURS_TO_COMPARE
        }

        return hours > hoursToCompare || (hours.toInt() == hoursToCompare && minutes >= 30)
    }

    private fun getLocation(edit: EditText, radio1: RadioButton, radio2: RadioButton): String? {
        return when {
            radio1.isChecked -> Config.GATE_3
            radio2.isChecked -> Config.GATE_4
            edit.text.toString().contains(Config.GATE_3_REGEX.toRegex()) -> null
            edit.text.toString().contains(Config.GATE_4_REGEX.toRegex()) -> null
            edit.text.isNotEmpty() -> edit.text.toString()
            else -> null
        }
    }

    private fun getTime(): Date {
        var hours = Config.MORNING_HOUR
        var minutes = Config.MORNING_MINUTE

        if (eveningTime.isChecked){
            hours = Config.EVENING_HOUR
            minutes = Config.EVENING_MINUTE
        }
        return Calendar.getInstance().apply {
            set(
                datePicker.year,
                datePicker.month,
                datePicker.dayOfMonth,
                hours,
                minutes,
                Config.SECONDS
            )
        }.time
    }
}