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
package com.example.cupcake.data

/**
 * Data class that represents the current UI state in terms of [quantity], [flavor],
 * selected [price]
 */
data class OrderUiState(
    /** Selected cupcake quantity (1, 6, 12) */
    val quantity: Int = 0,
    /** Flavor of the cupcakes in the order (such as "Chocolate", "Vanilla", etc..) */
    val flavor: String = "",
    /** Total price for the order */
    val price: String = "",
    /** 是否选择了全部口味套餐 */
    val isFlavorBundle: Boolean = false,
    /** 选择的配料列表 */
    val selectedToppings: List<String> = listOf(),
    /** 蛋糕价格 */
    val cakePrice: String = "",
    /** 配料总价 */
    val toppingsPrice: String = "",
    /** 是否四舍五入总价 */
    val isRounded: Boolean = false
)
