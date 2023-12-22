package com.example.rideon.model.database.room

import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rideon.RideOn
import com.example.rideon.model.Config.USER_NOT_FOUND
import com.example.rideon.model.data_classes.User
import kotlinx.coroutines.launch

class RoomAccountManager private constructor(): AndroidViewModel(application = RideOn.instance) {
    companion object {
        val instance: RoomAccountManager by lazy { RoomAccountManager() }
    }

    private val userDao = UserDatabase.getDatabase(RideOn.instance).userDao()
    private val userRepository = UserRepository(userDao)

    fun getUserCountFromRoom(fragment: Fragment, onSuccess: (Int) -> Unit) {
        viewModelScope.launch {
            userRepository.getUsersCount().observe(fragment.viewLifecycleOwner) {
                if(it != null)
                    onSuccess(it)
            }
        }
    }

    fun getLoggedInUser(
        fragment: Fragment,
        onSuccess: (User) -> Unit,
        onFailure: (String) -> Unit
    ){
        viewModelScope.launch {
            userRepository.getAllUsers().observe(fragment.viewLifecycleOwner) {
                if (it != null && it.isNotEmpty())
                    onSuccess(it[0])
                if(it.isEmpty())
                    onFailure(USER_NOT_FOUND)
            }
        }
    }

    fun insertUser(user: User){
        viewModelScope.launch {
            userRepository.insertUser(user = user)
        }
    }

    fun updateUserWallet(user: User, newBalance: Double){
        viewModelScope.launch {
            userRepository.updateUserWallet(user, newBalance)
        }
    }

    fun clearRoomDB(){
        viewModelScope.launch {
            userRepository.clearUsers()
        }
    }


}