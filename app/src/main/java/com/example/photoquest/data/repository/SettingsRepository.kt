package com.example.photoquest.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.photoquest.data.model.UserSettings
import com.example.photoquest.ui.extensions.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(
    private val context: Context,
) {

    companion object {
        private val questSearchRadius = doublePreferencesKey("quest_search_radius")
        private val showSearchArea = booleanPreferencesKey("show_search_area")
        private val searchAutomatically = booleanPreferencesKey("search_automatically")
    }

    val settingsFlow: Flow<UserSettings> = context.dataStore.data.map { settings ->

        UserSettings(
            questSearchRadius = settings[questSearchRadius] ?: 10.0,
            showSearchArea = settings[showSearchArea] ?: true,
            searchAutomatically = settings[searchAutomatically] ?: false,
        )
    }

    suspend fun saveSettings(userSettings: UserSettings) {

        context.dataStore.edit { settings ->

            settings[questSearchRadius] = userSettings.questSearchRadius
            settings[showSearchArea] = userSettings.showSearchArea
            settings[searchAutomatically] = userSettings.searchAutomatically
        }
    }
}
