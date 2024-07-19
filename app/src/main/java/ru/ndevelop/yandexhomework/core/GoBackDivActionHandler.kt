package ru.ndevelop.yandexhomework.core

import com.yandex.div.core.DivActionHandler
import com.yandex.div.core.DivViewFacade
import com.yandex.div.json.expressions.ExpressionResolver
import com.yandex.div2.DivAction
import ru.ndevelop.yandexhomework.core.listeners.GoBackListener

class GoBackDivActionHandler(
    private val goBackListener: GoBackListener
) : DivActionHandler() {
    override fun handleAction(
        action: DivAction,
        view: DivViewFacade,
        resolver: ExpressionResolver
    ): Boolean {
        val url =
            action.url?.evaluate(resolver) ?: return super.handleAction(action, view, resolver)

        return if (url.scheme == SCHEME_SAMPLE && handleSampleAction()) {
            true
        } else {
            super.handleAction(action, view, resolver)
        }
    }

    private fun handleSampleAction(): Boolean {
        goBackListener.goBack()
        return true
    }

    companion object {
        const val SCHEME_SAMPLE = "go-back"
    }
}