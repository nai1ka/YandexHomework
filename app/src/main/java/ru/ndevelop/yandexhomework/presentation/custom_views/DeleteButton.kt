package ru.ndevelop.yandexhomework.presentation.custom_views

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import ru.ndevelop.yandexhomework.R

class DeleteButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatButton(context, attrs, defStyleAttr) {


    fun enable() {
        isClickable = true
        setTextColor(ContextCompat.getColor(context, R.color.red))
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_delete_enabled, 0, 0, 0)
    }

    fun disable() {
        isClickable = false
        setTextColor(ContextCompat.getColor(context, R.color.gray_light))
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_delete_disabled, 0, 0, 0)
    }
}