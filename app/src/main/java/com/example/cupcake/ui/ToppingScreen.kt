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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cupcake.R
import com.example.cupcake.data.ToppingItem
import com.example.cupcake.ui.components.FormattedPriceLabel

/**
 * 配料选择界面
 */
@Composable
fun ToppingScreen(
    subtotal: String,
    selectedToppings: List<String>,
    onToppingsSelected: (List<String>) -> Unit,
    onNextButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 获取配料相关的字符串资源
    val strawberriesName = stringResource(R.string.strawberries)
    val strawberriesDesc = stringResource(R.string.strawberries_description)
    val strawberriesPrice = stringResource(R.string.strawberries_price)
    
    val blueberriesName = stringResource(R.string.blueberries)
    val blueberriesDesc = stringResource(R.string.blueberries_description)
    val blueberriesPrice = stringResource(R.string.blueberries_price)
    
    val orangesName = stringResource(R.string.oranges)
    val orangesDesc = stringResource(R.string.oranges_description)
    val orangesPrice = stringResource(R.string.oranges_price)
    
    val noToppingsText = stringResource(R.string.no_toppings_selected)
    
    // 直接在Composable函数中定义配料列表
    val toppingsList = listOf(
        ToppingItem(
            name = strawberriesName,
            description = strawberriesDesc,
            price = 5.0,
            priceString = strawberriesPrice,
            imageRes = R.drawable.strawberries
        ),
        ToppingItem(
            name = blueberriesName,
            description = blueberriesDesc,
            price = 10.0,
            priceString = blueberriesPrice,
            imageRes = R.drawable.blueberries
        ),
        ToppingItem(
            name = orangesName,
            description = orangesDesc,
            price = 6.0,
            priceString = orangesPrice,
            imageRes = R.drawable.oranges
        )
    )
    val selectedToppingsList = remember { mutableStateListOf<String>().apply { addAll(selectedToppings) } }
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 显示配料选择标题和说明
        Text(
            text = stringResource(R.string.topping_selection_title),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.padding_medium),
                top = dimensionResource(R.dimen.padding_medium),
                end = dimensionResource(R.dimen.padding_medium)
            )
        )
        
        Text(
            text = stringResource(R.string.topping_selection_description),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.padding_medium),
                bottom = dimensionResource(R.dimen.padding_medium),
                end = dimensionResource(R.dimen.padding_medium)
            )
        )
        
        // 显示已选择的配料
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            ) {
                Text(
                    text = stringResource(R.string.selected_toppings),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                if (selectedToppingsList.isEmpty()) {
                    Text(
                        text = noToppingsText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                } else {
                    selectedToppingsList.forEach { topping ->
                        val toppingItem = toppingsList.find { it.name == topping }
                        Text(
                            text = "• ${toppingItem?.name ?: topping} (${toppingItem?.priceString ?: ""})",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 使用LazyColumn显示配料选项
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = dimensionResource(R.dimen.padding_medium))
        ) {
            items(toppingsList) { toppingItem ->
                ToppingItemCard(
                    toppingItem = toppingItem,
                    isSelected = selectedToppingsList.contains(toppingItem.name),
                    onSelectionChanged = { isSelected ->
                        if (isSelected) {
                            if (!selectedToppingsList.contains(toppingItem.name)) {
                                selectedToppingsList.add(toppingItem.name)
                            }
                        } else {
                            selectedToppingsList.remove(toppingItem.name)
                        }
                        onToppingsSelected(selectedToppingsList.toList())
                    },
                    modifier = Modifier.padding(vertical = 4.dp)
                )
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
                onClick = onNextButtonClicked
            ) {
                Text(stringResource(R.string.next))
            }
        }
    }
}

/**
 * 配料项卡片组件
 */
@Composable
fun ToppingItemCard(
    toppingItem: ToppingItem,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        colors = if (isSelected) 
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        else 
            CardDefaults.cardColors()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(
                    value = isSelected,
                    onValueChange = { onSelectionChanged(it) }
                )
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 配料图片
            Image(
                painter = painterResource(id = toppingItem.imageRes),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = toppingItem.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = toppingItem.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = toppingItem.priceString,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onSelectionChanged(it) }
            )
        }
    }
} 