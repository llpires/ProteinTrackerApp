package com.proteintracker.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.em

@Composable
fun ProteinCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    color: Color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
    onClick: (() -> Unit)? = null
) {
    val cardModifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 6.dp)
    
    val content = Card(
        modifier = cardModifier,
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerHighest
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                icon?.let {
                    Icon(
                        painter = it,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(24.dp).padding(end = 12.dp)
                    )
                }
                Column {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = value,
                        fontSize = 28.sp,
                        color = color,
                        fontWeight = FontWeight.Bold
                    )
                    subtitle?.let {
                        Text(
                            text = it,
                            fontSize = 12.sp,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            }
            if (onClick != null) {
                Icon(
                    painter = androidx.compose.material.icons.defaults.ExpandMore,
                    contentDescription = null,
                    tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
    
    if (onClick != null) {
        androidx.compose.foundation.clickable(content, onClick)
    } else {
        content
    }
}

@Composable
fun ProgressRing(
    modifier: Modifier = Modifier,
    progress: Float, // 0.0 to 1.0
    size: Int = 120,
    strokeWidth: Int = 12,
    trackColor: Color = androidx.compose.material3.MaterialTheme.colorScheme.outlineVariant,
    progressColor: Color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
    showPercentage: Boolean = true,
    centerText: String? = null
) {
    import androidx.compose.foundation.Canvas
    import androidx.compose.ui.graphics.BlendMode
    import androidx.compose.ui.graphics.Paint
    import androidx.compose.ui.graphics.Paint.Style
    import androidx.compose.ui.graphics.drawscope.drawArc
    import androidx.compose.ui.unit.dp
    
    val modifier = modifier.size(size.dp)
    
    Canvas(modifier = modifier) {
        val radius = (size - strokeWidth) / 2f
        val center = size / 2f
        
        // Track
        val trackPaint = Paint().apply {
            style = Style.STROKE
            strokeWidth = strokeWidth.toFloat()
            color = trackColor
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
        }
        drawCircle(trackPaint, center = androidx.compose.ui.geometry.Offset(center, center), radius = radius)
        
        // Progress
        if (progress > 0) {
            val progressPaint = Paint().apply {
                style = Style.STROKE
                strokeWidth = strokeWidth.toFloat()
                color = progressColor
                isAntiAlias = true
                strokeCap = Paint.Cap.ROUND
            }
            val sweepAngle = 360 * progress.coerceIn(0f, 1f)
            drawArc(
                paint = progressPaint,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = androidx.compose.ui.geometry.Offset(
                    (size - (size - strokeWidth)) / 2f,
                    (size - (size - strokeWidth)) / 2f
                ),
                size = androidx.compose.ui.geometry.Size(size - strokeWidth, size - strokeWidth)
            )
        }
        
        // Center text
        if (showPercentage || centerText != null) {
            val text = centerText ?: "${(progress * 100).roundToInt()}%"
            drawText(
                text = androidx.compose.ui.text.AnnotatedString(text),
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                fontSize = (size / 4).dp,
                fontWeight = FontWeight.Bold,
                topLeft = androidx.compose.ui.geometry.Offset(
                    center - (text.length * size / 8),
                    center - size / 8
                )
            )
        }
    }
}

@Composable
fun MealTypeChip(
    mealType: com.proteintracker.data.model.MealType,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    import androidx.compose.material3.Chip
    import androidx.compose.material3.ChipDefaults
    
    val color = if (selected) {
        androidx.compose.material3.MaterialTheme.colorScheme.primary
    } else {
        androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerHighest
    }
    
    val textColor = if (selected) {
        androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
    } else {
        androidx.compose.material3.MaterialTheme.colorScheme.onSurface
    }
    
    Chip(
        modifier = modifier,
        onClick = onClick,
        selected = selected,
        onSelectedChange = { _ -> onClick?.invoke() },
        colors = ChipDefaults.elevatedChipColors(
            containerColor = color,
            selectedContainerColor = color,
            labelColor = textColor,
            selectedLabelColor = textColor
        )
    ) {
        Text(
            text = mealType.displayName,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun EmptyState(
    modifier: Modifier = Modifier.fillMaxSize(),
    icon: ImageVector,
    title: String,
    message: String,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.Button
    import androidx.compose.material3.Icon
    import androidx.compose.ui.Alignment
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        arrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Text(
            text = message,
            fontSize = 16.sp,
            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
        actionText?.let { text ->
            onAction?.let { action ->
                Button(onClick = action, modifier = Modifier.padding(top = 8.dp)) {
                    Text(text = text)
                }
            }
        }
    }
}

@Composable
fun LoadingOverlay(
    modifier: Modifier = Modifier.fillMaxSize(),
    message: String = "Carregando..."
) {
    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.material3.CircularProgressIndicator
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    
    Box(modifier = modifier.background(Color.Black.copy(alpha = 0.3f))) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            arrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = message,
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}