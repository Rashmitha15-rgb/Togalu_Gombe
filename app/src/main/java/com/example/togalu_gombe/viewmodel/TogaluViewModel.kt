package com.example.togalu_gombe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.togalu_gombe.data.Play
import com.example.togalu_gombe.data.Puppet
import com.example.togalu_gombe.data.Scene
import com.example.togalu_gombe.data.TogaluDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TogaluViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = TogaluDatabase.getDatabase(application, viewModelScope).togaluDao()

    val allPlays: StateFlow<List<Play>> = dao.getAllPlays()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allPuppets: StateFlow<List<Puppet>> = dao.getAllPuppets()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val historyVideos = dao.getAllHistoryVideos()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val storeItems = dao.getAllStoreItems()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _scenesForCurrentPlay = MutableStateFlow<List<Scene>>(emptyList())
    val scenesForCurrentPlay: StateFlow<List<Scene>> = _scenesForCurrentPlay.asStateFlow()

    private val _isKannada = MutableStateFlow(false)
    val isKannada: StateFlow<Boolean> = _isKannada.asStateFlow()

    fun toggleLanguage() {
        _isKannada.value = !_isKannada.value
    }

    fun loadScenesForPlay(playId: Int) {
        viewModelScope.launch {
            dao.getScenesForPlay(playId).collect { scenes ->
                _scenesForCurrentPlay.value = scenes
            }
        }
    }
}
