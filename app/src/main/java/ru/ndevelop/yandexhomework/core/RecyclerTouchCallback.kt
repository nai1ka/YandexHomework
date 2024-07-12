package ru.ndevelop.yandexhomework.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.ndevelop.yandexhomework.R
import ru.ndevelop.yandexhomework.core.listeners.OnItemSwipeListener

open class RecyclerTouchCallback(
    private val applicationContext: Context,
    private val onItemSwipeListener: OnItemSwipeListener
) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.LEFT) {
            onItemSwipeListener.onLeftSwipe(viewHolder.adapterPosition)

        } else {
            onItemSwipeListener.onRightSwipe(viewHolder.adapterPosition)
        }
    }


    private val redPaint = Paint().apply {
        color = ContextCompat.getColor(applicationContext, R.color.red)
    }

    private val greenPaint = Paint().apply {
        color = ContextCompat.getColor(applicationContext, R.color.green)
    }

    private val deleteIcon =
        ContextCompat.getDrawable(applicationContext, R.drawable.ic_delete)
    private val doneButton = ContextCompat.getDrawable(applicationContext, R.drawable.ic_done)
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView
            // Draw red background on swipe
            if (dX < 0) { // Swiping to the left
                c.drawRect(
                    itemView.right.toFloat() + dX,
                    itemView.top.toFloat(),
                    itemView.right.toFloat(),
                    itemView.bottom.toFloat(),
                    redPaint
                )

                deleteIcon?.let {
                    val iconMargin = (itemView.height - it.intrinsicHeight) / 2
                    val iconTop = itemView.top + iconMargin
                    val iconBottom = iconTop + it.intrinsicHeight
                    val iconLeft = itemView.right + dX.toInt() + iconMargin
                    val iconRight =
                        itemView.right + dX.toInt() + iconMargin + it.intrinsicWidth

                    if (iconRight < itemView.right) {
                        it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        it.draw(c)
                    }
                }
            } else if (dX > 0) {
                c.drawRect(
                    itemView.left.toFloat(),
                    itemView.top.toFloat(),
                    itemView.left.toFloat() + dX,
                    itemView.bottom.toFloat(),
                    greenPaint
                )

                doneButton?.let {
                    val iconMargin = (itemView.height - it.intrinsicHeight) / 2
                    val iconTop = itemView.top + iconMargin
                    val iconBottom = iconTop + it.intrinsicHeight
                    val iconRight = itemView.left + dX.toInt() - iconMargin
                    val iconLeft = iconRight - it.intrinsicWidth
                    if (iconLeft >= itemView.left) {
                        it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        it.draw(c)
                    }
                }
            }

            super.onChildDraw(
                c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
        }
    }
}