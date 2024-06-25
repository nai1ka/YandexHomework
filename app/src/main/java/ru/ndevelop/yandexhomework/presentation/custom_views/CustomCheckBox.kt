package ru.ndevelop.yandexhomework.presentation.custom_views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import ru.ndevelop.yandexhomework.R

class CustomCheckBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatCheckBox(context, attrs, defStyleAttr) {

    var isUrgent = false
        set(value) {
            if (value != field) {
                field = value
                updateUI()
            }
        }

    fun setCompleted(isCompleted: Boolean) {
        isChecked = isCompleted
        updateUI()
    }

    init {
        updateUI()
        setOnClickListener {
            updateUI()
        }
    }

    private fun updateUI() {
        if (isChecked) {
            setBackgroundDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_checkbox_checked
                )
            )
        } else {
            if (isUrgent) {
                setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_checkbox_urgent
                    )
                )
            } else {
                setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_checkbox_normal
                    )
                )
            }
        }
    }
}
