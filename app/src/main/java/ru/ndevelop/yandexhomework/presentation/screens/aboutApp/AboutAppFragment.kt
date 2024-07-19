package ru.ndevelop.yandexhomework.presentation.screens.aboutApp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.glide.GlideDivImageLoader
import org.json.JSONObject
import ru.ndevelop.yandexhomework.core.Div2ViewFactory
import ru.ndevelop.yandexhomework.core.GoBackDivActionHandler
import ru.ndevelop.yandexhomework.core.listeners.GoBackListener
import java.io.IOException

class AboutAppFragment : Fragment() {

    fun Context.readJsonFromAssets(fileName: String): JSONObject? {
        var jsonString: String? = null
        try {
            jsonString = assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }

        return JSONObject(jsonString)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val divJson = requireContext().readJsonFromAssets("sample.json") ?: return null

        val templatesJson = divJson.optJSONObject("templates")
        val cardJson = divJson.getJSONObject("card")

        val divContext = Div2Context(
            baseContext = requireActivity(),
            configuration = createDivConfiguration(),
            lifecycleOwner = this
        )

        val divView = Div2ViewFactory(divContext, templatesJson).createView(cardJson)
        return divView
    }

    private val goBackListener = object : GoBackListener {
        override fun goBack() {
            findNavController().popBackStack()
        }
    }

    private fun createDivConfiguration(): DivConfiguration {
        return DivConfiguration.Builder(GlideDivImageLoader(requireActivity().applicationContext))
             .actionHandler(GoBackDivActionHandler(goBackListener))
            .visualErrorsEnabled(true)
            .build()
    }
}