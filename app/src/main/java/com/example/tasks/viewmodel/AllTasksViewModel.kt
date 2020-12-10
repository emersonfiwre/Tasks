package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.TaskRepository

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {
    private val mTaskRepository = TaskRepository(application)
    private var mTaskFilter: Int = 0


    private var mValidation = MutableLiveData<ValidationListener>()
    var validation: LiveData<ValidationListener> = mValidation

    private var mList = MutableLiveData<List<TaskModel>>()
    var list: LiveData<List<TaskModel>> = mList

    fun list(taskFilter: Int) {
        mTaskFilter = taskFilter
        val listener = object : APIListener<List<TaskModel>> {
            override fun onSuccess(model: List<TaskModel>) {
                mList.value = model
            }

            override fun onFailure(str: String) {
                mList.value = arrayListOf()
                mValidation.value = ValidationListener(str)
            }
        }

        if (taskFilter == TaskConstants.FILTER.ALL) {
            mTaskRepository.all(listener)
        } else if (taskFilter == TaskConstants.FILTER.NEXT) {
            mTaskRepository.nextWeek(listener)
        } else {
            mTaskRepository.overdue(listener)
        }
    }

    fun complete(id: Int) {
        updateStatus(id, true)
    }

    fun undo(id: Int) {
        updateStatus(id, false)
    }

    fun updateStatus(id: Int, complete: Boolean) {
        mTaskRepository.updateStatus(id, complete, object : APIListener<Boolean> {
            override fun onSuccess(model: Boolean) {
                list(mTaskFilter)
            }

            override fun onFailure(str: String) {
            }
        })
    }

    fun delete(id: Int) {
        mTaskRepository.delete(id, object : APIListener<Boolean> {
            override fun onSuccess(model: Boolean) {
                list(mTaskFilter)
                mValidation.value = ValidationListener()
            }

            override fun onFailure(str: String) {
                mValidation.value = ValidationListener(str)
            }
        })
    }


}