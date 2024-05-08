package com.threegroup.tobedated

import androidx.lifecycle.ViewModel
import com.threegroup.tobedated.shareclasses.Repository

class RootViewModel(private var repository: Repository) : ViewModel() {
    fun updateNotificationCounts(callback: (totalNotificationCount: Int) -> Unit) {
        repository.updateNotificationCounts(callback)
    }
}