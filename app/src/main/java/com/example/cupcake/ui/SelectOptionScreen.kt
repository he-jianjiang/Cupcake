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

import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cupcake.R
import com.example.cupcake.data.FlavorItem
import com.example.cupcake.ui.components.FormattedPriceLabel
import com.example.cupcake.ui.theme.CupcakeTheme

/**
 * Composable that displays the list of items as [RadioButton] options with images and descriptions,
 * [onSelectionChanged] lambda that notifies the parent composable when a new value is selected,
 * [onCancelButtonClicked] lambda that cancels the order when user clicks cancel and
 * [onNextButtonClicked] lambda that triggers the navigation to next screen
 */
@Composable
fun SelectOptionScreen(
    subtotal: String,
    options: List<String>,
    onSelectionChanged: (String) -> Unit = {},
    onCancelButtonClicked: () -> Unit = {},
    onNextButtonClicked: () -> Unit = {},
    modifier: Modifier = Modifier,
    isFlavorScreen: Boolean = false,
    onFlavorPriceUpdated: ((Double) -> Unit)? = null
) {
    var selectedValue by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (isFlavorScreen) {
            // 显示口味选择标题和说明
            Text(
                text = stringResource(R.string.flavor_selection_title),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(
                    start = dimensionResource(R.dimen.padding_medium),
                    top = dimensionResource(R.dimen.padding_medium),
                    end = dimensionResource(R.dimen.padding_medium)
                )
            )
            
            Text(
                text = stringResource(R.string.flavor_selection_description),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(
                    start = dimensionResource(R.dimen.padding_medium),
                    bottom = dimensionResource(R.dimen.padding_medium),
                    end = dimensionResource(R.dimen.padding_medium)
                )
            )
            
            // 获取口味名称和描述的字符串
            val packageAllFlavors = stringResource(R.string.package_all_flavors)
            val packageAllFlavorsDesc = stringResource(R.string.package_all_flavors_description)
            val packageAllFlavorsPrice = stringResource(R.string.package_all_flavors_price)
            val vanillaName = stringResource(R.string.vanilla)
            val chocolateName = stringResource(R.string.chocolate)
            val redVelvetName = stringResource(R.string.red_velvet)
            val saltedCaramelName = stringResource(R.string.salted_caramel)
            val coffeeName = stringResource(R.string.coffee)
            
            // 直接在这里定义口味列表
            val flavorItems = listOf(
                // 添加全部口味套餐选项
                FlavorItem(
                    name = packageAllFlavors,
                    description = packageAllFlavorsDesc,
                    price = 20.0,
                    priceString = "RM20.0",
                    imageRes = R.drawable.cupcake
                ),
                FlavorItem(
                    name = vanillaName,
                    description = "经典香草口味，口感顺滑细腻",
                    price = 15.2,
                    priceString = "RM15.2",
                    imageRes = R.drawable.vanilla
                ),
                FlavorItem(
                    name = chocolateName,
                    description = "使用优质可可制成的浓郁巧克力口味",
                    price = 10.8,
                    priceString = "RM10.8",
                    imageRes = R.drawable.chocolate
                ),
                FlavorItem(
                    name = redVelvetName,
                    description = "优雅的红丝绒，带有可可和奶油芝士的味道",
                    price = 20.6,
                    priceString = "RM20.6",
                    imageRes = R.drawable.red_velvet
                ),
                FlavorItem(
                    name = saltedCaramelName,
                    description = "甜美的焦糖配以恰到好处的海盐",
                    price = 18.9,
                    priceString = "RM18.9",
                    imageRes = R.drawable.salted_caramel
                ),
                FlavorItem(
                    name = coffeeName,
                    description = "为咖啡爱好者准备的咖啡味蛋糕",
                    price = 24.8,
                    priceString = "RM24.8",
                    imageRes = R.drawable.coffee
                )
            )
            
            // 使用LazyColumn显示带图片和描述的口味选项
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = dimensionResource(R.dimen.padding_medium))
            ) {
                items(flavorItems) { flavorItem ->
                    FlavorItemCard(
                        flavorItem = flavorItem,
                        selected = selectedValue == flavorItem.name,
                        onSelected = {
                            selectedValue = flavorItem.name
                            onSelectionChanged(flavorItem.name)
                            // 更新价格
                            onFlavorPriceUpdated?.invoke(flavorItem.price)
                        },
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        } else {
            // 原有的选项列表显示逻辑
            Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
                options.forEach { item ->
                    Row(
                        modifier = Modifier.selectable(
                            selected = selectedValue == item,
                            onClick = {
                                selectedValue = item
                                onSelectionChanged(item)
                            }
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedValue == item,
                            onClick = {
                                selectedValue = item
                                onSelectionChanged(item)
                            }
                        )
                        Text(item)
                    }
                }
            }
        }
        
        Divider(
            thickness = dimensionResource(R.dimen.thickness_divider),
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.padding_medium),
                end = dimensionResource(R.dimen.padding_medium),
                bottom = dimensionResource(R.dimen.padding_medium)
            )
        )
        
        FormattedPriceLabel(
            subtotal = subtotal,
            modifier = Modifier
                .align(Alignment.End)
                .padding(
                    end = dimensionResource(R.dimen.padding_medium),
                    bottom = dimensionResource(R.dimen.padding_medium)
                )
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = onCancelButtonClicked
            ) {
                Text(stringResource(R.string.cancel))
            }
            Button(
                modifier = Modifier.weight(1f),
                // the button is enabled when the user makes a selection
                enabled = selectedValue.isNotEmpty(),
                onClick = onNextButtonClicked
            ) {
                Text(stringResource(R.string.next))
            }
        }
    }
}

/**
 * 口味项卡片组件，显示口味图片、名称和描述
 */
@Composable
fun FlavorItemCard(
    flavorItem: FlavorItem,
    selected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    val packageAllFlavors = stringResource(R.string.package_all_flavors)
    val packageAllFlavorsPrice = stringResource(R.string.package_all_flavors_price)
    
    val isBundle = flavorItem.name == packageAllFlavors
    
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (selected) 8.dp else if (isBundle) 6.dp else 2.dp
        ),
        colors = when {
            selected -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            isBundle -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            else -> CardDefaults.cardColors()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .selectable(
                    selected = selected,
                    onClick = onSelected
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 口味图片
            Image(
                painter = painterResource(id = flavorItem.imageRes),
                contentDescription = null,
                modifier = Modifier.size(if (isBundle) 100.dp else 80.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = flavorItem.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isBundle) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = flavorItem.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // 显示价格
                Text(
                    text = if (isBundle) packageAllFlavorsPrice else flavorItem.priceString,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isBundle) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                )
                
                if (isBundle) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "节省超过70%!",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            
            RadioButton(
                selected = selected,
                onClick = onSelected
            )
        }
    }
}

@Preview
@Composable
fun SelectOptionPreview() {
    CupcakeTheme {
        SelectOptionScreen(
            subtotal = "$12.00",
            options = listOf("Option 1", "Option 2", "Option 3", "Option 4"),
            modifier = Modifier.fillMaxHeight(),
            isFlavorScreen = true
        )
    }
}
