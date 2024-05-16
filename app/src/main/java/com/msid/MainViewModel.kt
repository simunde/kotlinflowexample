package com.msid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    val countDownFlow = flow<Int> {
        val startingValue = 5
        var currentValue= startingValue
        emit(startingValue)
        while (currentValue > 0) {
            delay(1000L)
            currentValue--
            emit(currentValue)
        }
    }

    init {
        collectFlow()
    }

    private fun collectFlow(){

//        countDownFlow.onEach {
//            println("Inside something $it")
//        }.launchIn(viewModelScope)


        viewModelScope.launch {
            countDownFlow
                .filter {
                    it%2==0
                }
                .map {
                    it*it
                }
                .onEach {
                    println(it)
                }
                .collect{
                time->
                println("The current time is $time")//This prints 16,4,0
            }
        }


        viewModelScope.launch {
            val reducedResult = countDownFlow
                .map {
                    it*it
                }
                .reduce{a,b->a+b}
            println("The reduced Result $reducedResult")//It return 55=5^2+4^2+3^2+2^2+1^
        }
    }
}