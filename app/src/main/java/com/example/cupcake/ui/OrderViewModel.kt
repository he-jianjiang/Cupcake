/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.cupcake.ui

import androidx.lifecycle.ViewModel
import com.example.cupcake.data.OrderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/** Default price for cupcake if not specified */
private const val DEFAULT_PRICE_PER_CUPCAKE = 2.00

/** 全部口味套餐价格 */
private const val ALL_FLAVORS_BUNDLE_PRICE = 20.00

/** 配料价格 */
private val TOPPING_PRICES = mapOf(
    "草莓" to 5.00,
    "蓝莓" to 10.00,
    "橙子" to 6.00
)

/**
 * [OrderViewModel] holds information about a cupcake order in terms of quantity, flavor, and
 * toppings. It also knows how to calculate the total price based on these order details.
 */
class OrderViewModel : ViewModel() {

    /**
     * Cupcake state for this order
     */
    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()
    
    // 当前选择的蛋糕单价
    private var currentPricePerCupcake = DEFAULT_PRICE_PER_CUPCAKE

    /**
     * Set the quantity [numberCupcakes] of cupcakes for this order's state and update the price
     */
    fun setQuantity(numberCupcakes: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                quantity = numberCupcakes,
                cakePrice = calculateCakePrice(quantity = numberCupcakes),
                price = calculateTotalPrice(
                    cakePrice = calculateCakePrice(quantity = numberCupcakes, isRaw = true),
                    toppingsPrice = calculateToppingsPrice(toppings = currentState.selectedToppings, isRaw = true)
                )
            )
        }
    }

    /**
     * Set the [desiredFlavor] of cupcakes for this order's state.
     * Only 1 flavor can be selected for the whole order.
     */
    fun setFlavor(desiredFlavor: String) {
        // 检查是否是全部口味套餐
        val isBundle = desiredFlavor == "全口味套餐"
        
        _uiState.update { currentState ->
            currentState.copy(
                flavor = desiredFlavor,
                isFlavorBundle = isBundle
            )
        }
    }
    
    /**
     * Update the price per cupcake based on selected flavor
     */
    fun updatePrice(pricePerCupcake: Double) {
        currentPricePerCupcake = pricePerCupcake
        _uiState.update { currentState ->
            val cakePrice = calculateCakePrice(
                quantity = currentState.quantity,
                isRaw = true
            )
            val toppingsPrice = calculateToppingsPrice(
                toppings = currentState.selectedToppings,
                isRaw = true
            )
            
            currentState.copy(
                cakePrice = calculateCakePrice(quantity = currentState.quantity),
                price = calculateTotalPrice(cakePrice = cakePrice, toppingsPrice = toppingsPrice)
            )
        }
    }

    /**
     * 设置选择的配料
     */
    fun setSelectedToppings(toppings: List<String>) {
        val toppingsPrice = calculateToppingsPrice(toppings = toppings, isRaw = true)
        val cakePrice = calculateCakePrice(quantity = _uiState.value.quantity, isRaw = true)
        
        _uiState.update { currentState ->
            currentState.copy(
                selectedToppings = toppings,
                toppingsPrice = calculateToppingsPrice(toppings = toppings),
                price = calculateTotalPrice(cakePrice = cakePrice, toppingsPrice = toppingsPrice)
            )
        }
    }

    /**
     * Reset the order state
     */
    fun resetOrder() {
        currentPricePerCupcake = DEFAULT_PRICE_PER_CUPCAKE
        _uiState.value = OrderUiState(
            isFlavorBundle = false,
            selectedToppings = listOf(),
            cakePrice = "",
            toppingsPrice = ""
        )
    }

    /**
     * 计算蛋糕价格
     */
    private fun calculateCakePrice(
        quantity: Int = _uiState.value.quantity,
        isRaw: Boolean = false
    ): String {
        val calculatedPrice = if (_uiState.value.isFlavorBundle) {
            ALL_FLAVORS_BUNDLE_PRICE
        } else {
            quantity * currentPricePerCupcake
        }
        
        return if (isRaw) {
            calculatedPrice.toString()
        } else {
            String.format("RM%.2f", calculatedPrice)
        }
    }
    
    /**
     * 计算配料价格
     */
    private fun calculateToppingsPrice(
        toppings: List<String> = _uiState.value.selectedToppings,
        isRaw: Boolean = false
    ): String {
        var calculatedPrice = 0.0
        
        // 添加配料价格
        for (topping in toppings) {
            calculatedPrice += TOPPING_PRICES[topping] ?: 0.0
        }
        
        return if (isRaw) {
            calculatedPrice.toString()
        } else {
            String.format("RM%.2f", calculatedPrice)
        }
    }
    
    /**
     * 设置是否四舍五入总价
     */
    fun toggleRoundPrice() {
        _uiState.update { currentState ->
            val newIsRounded = !currentState.isRounded
            val cakePrice = calculateCakePrice(
                quantity = currentState.quantity,
                isRaw = true
            )
            val toppingsPrice = calculateToppingsPrice(
                toppings = currentState.selectedToppings,
                isRaw = true
            )
            
            currentState.copy(
                isRounded = newIsRounded,
                price = calculateTotalPrice(
                    cakePrice = cakePrice, 
                    toppingsPrice = toppingsPrice,
                    isRounded = newIsRounded
                )
            )
        }
    }

    /**
     * 计算总价
     */
    private fun calculateTotalPrice(
        cakePrice: String,
        toppingsPrice: String,
        isRounded: Boolean = _uiState.value.isRounded
    ): String {
        val cakePriceValue = cakePrice.toDoubleOrNull() ?: 0.0
        val toppingsPriceValue = toppingsPrice.toDoubleOrNull() ?: 0.0
        
        val totalPrice = cakePriceValue + toppingsPriceValue
        
        return if (isRounded) {
            // 四舍五入到整数
            String.format("RM%.0f", Math.round(totalPrice).toDouble())
        } else {
            String.format("RM%.2f", totalPrice)
        }
    }
}
