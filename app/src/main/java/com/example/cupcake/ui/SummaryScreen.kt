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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cupcake.R
import com.example.cupcake.data.OrderUiState
import com.example.cupcake.ui.components.FormattedPriceLabel
import com.example.cupcake.ui.theme.CupcakeTheme

/**
 * This composable expects [orderUiState] that represents the order state, [onCancelButtonClicked]
 * lambda that triggers canceling the order and passes the final order to [onSendButtonClicked]
 * lambda
 */
@Composable
fun OrderSummaryScreen(
    orderUiState: OrderUiState,
    onCancelButtonClicked: () -> Unit,
    onSendButtonClicked: () -> Unit,
    onRoundPriceClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val resources = LocalContext.current.resources

    val numberOfCupcakes = resources.getQuantityString(
        R.plurals.cupcakes,
        orderUiState.quantity,
        orderUiState.quantity
    )
    
    val toppingsText = if (orderUiState.selectedToppings.isEmpty()) {
        stringResource(R.string.no_toppings_selected)
    } else {
        orderUiState.selectedToppings.joinToString(", ")
    }
    
    //Load and format a string resource with the parameters.
    val orderSummary = stringResource(
        R.string.order_details,
        numberOfCupcakes,
        orderUiState.flavor,
        toppingsText,
        orderUiState.price
    )
    val newOrder = stringResource(R.string.new_cupcake_order)
    //Create a list of order summary to display
    val items = if (orderUiState.isFlavorBundle) {
        listOf(
            // Summary line 1: display selected quantity
            Pair(stringResource(R.string.quantity), numberOfCupcakes),
            // Summary line 2: display selected flavor
            Pair(stringResource(R.string.flavor), stringResource(R.string.package_all_flavors)),
            // 添加套餐详情
            Pair("套餐详情", "香草, 巧克力, 红丝绒, 海盐焦糖, 咖啡"),
            // 添加配料详情
            Pair(stringResource(R.string.toppings), toppingsText)
        )
    } else {
        listOf(
            // Summary line 1: display selected quantity
            Pair(stringResource(R.string.quantity), numberOfCupcakes),
            // Summary line 2: display selected flavor
            Pair(stringResource(R.string.flavor), orderUiState.flavor),
            // 添加配料详情
            Pair(stringResource(R.string.toppings), toppingsText)
        )
    }

    // 准备口味图片列表
    val flavorImages = if (orderUiState.isFlavorBundle) {
        listOf(stringResource(R.string.vanilla), stringResource(R.string.chocolate), 
               stringResource(R.string.red_velvet), stringResource(R.string.salted_caramel), 
               stringResource(R.string.coffee))
    } else if (orderUiState.flavor.isNotEmpty()) {
        listOf(orderUiState.flavor)
    } else {
        emptyList()
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(dimensionResource(R.dimen.padding_medium))
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            // 添加订单标题
            Text(
                text = stringResource(R.string.order_summary),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // 订单信息卡片
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
                ) {
                    items.forEach { item ->
                        Text(
                            text = item.first.uppercase(),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        if (item.first == stringResource(R.string.flavor).uppercase() && flavorImages.isNotEmpty()) {
                            // 显示口味图片
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(flavorImages) { flavor ->
                                    Card(
                                        modifier = Modifier.size(60.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = getFlavorImageResource(flavor)),
                                            contentDescription = flavor,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(4.dp)
                                        )
                                    }
                                }
                            }
                        }
                        
                        Text(
                            text = item.second, 
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        if (item.first == stringResource(R.string.toppings).uppercase() && 
                            orderUiState.selectedToppings.isNotEmpty()) {
                            // 显示配料图片
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(orderUiState.selectedToppings) { topping ->
                                    Card(
                                        modifier = Modifier.size(50.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = getToppingImageResource(topping)),
                                            contentDescription = topping,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(4.dp)
                                        )
                                    }
                                }
                            }
                        }
                        
                        Divider(thickness = dimensionResource(R.dimen.thickness_divider))
                    }
                    
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                    
                    // 价格详情卡片
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.price_details),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                
                                // 四舍五入开关
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(R.string.round_price_description),
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Switch(
                                        checked = orderUiState.isRounded,
                                        onCheckedChange = { onRoundPriceClicked() },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                                        )
                                    )
                                }
                            }
                            
                            Divider()
                            
                            // 蛋糕价格
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(R.string.cake_price),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = orderUiState.cakePrice,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            
                            // 配料价格
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(R.string.toppings_price),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = orderUiState.toppingsPrice,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            
                            Divider()
                            
                            // 总价
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(R.string.total),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = orderUiState.price,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // 按钮区域
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onSendButtonClicked
                ) {
                    Text(stringResource(R.string.send))
                }
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCancelButtonClicked
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        }
    }
}

/**
 * 获取口味对应的图片资源ID
 */
private fun getFlavorImageResource(flavor: String): Int {
    return when (flavor) {
        "香草" -> R.drawable.vanilla
        "巧克力" -> R.drawable.chocolate
        "红丝绒" -> R.drawable.red_velvet
        "海盐焦糖" -> R.drawable.salted_caramel
        "咖啡" -> R.drawable.coffee
        "全口味套餐" -> R.drawable.cupcake
        else -> R.drawable.cupcake
    }
}

/**
 * 获取配料对应的图片资源ID
 */
private fun getToppingImageResource(topping: String): Int {
    return when (topping) {
        "草莓" -> R.drawable.strawberries
        "蓝莓" -> R.drawable.blueberries
        "橙子" -> R.drawable.oranges
        else -> R.drawable.cupcake
    }
}

@Preview
@Composable
fun OrderSummaryPreview() {
    CupcakeTheme {
        OrderSummaryScreen(
            orderUiState = OrderUiState(
                quantity = 6,
                flavor = "Chocolate",
                price = "RM42.00",
                selectedToppings = listOf("Strawberries", "Blueberries"),
                cakePrice = "RM27.00",
                toppingsPrice = "RM15.00",
                isRounded = false
            ),
            onCancelButtonClicked = {},
            onSendButtonClicked = {},
            onRoundPriceClicked = {},
            modifier = Modifier.fillMaxHeight()
        )
    }
}
