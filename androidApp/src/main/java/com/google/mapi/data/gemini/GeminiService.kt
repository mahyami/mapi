package com.google.mapi.data.gemini

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.TextPart
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.google.mapi.data.GOOGLE_GEN_AI_KEY
import com.google.mapi.data.local.PlacesDao
import java.time.LocalDateTime
import javax.inject.Inject

class GeminiService @Inject constructor(
    private val placesDao: PlacesDao,
) {

    suspend fun sendMessage(prompt: String): GenerateContentResponse {
        val model = GenerativeModel(
            modelName = "gemini-1.5-pro",
            apiKey = GOOGLE_GEN_AI_KEY,
            generationConfig = buildGenerationConfig(),
            systemInstruction = buildSystemInstruction()
        )

        return model
            .startChat()
            .sendMessage(prompt)
    }

    private suspend fun buildSystemInstruction(): Content {
        val modelKnowledgeSource = buildModelKnowledgeSourceFromUserPlaces()
        return content("system") {
            parts = mutableListOf(
                TextPart(
                    "Hello! I'm here to help you find the perfect café or restaurant or any place you are craving from your saved google places list." +
                            "The user will describe what s.he is looking for and I will find it. "
                ),
                TextPart(modelKnowledgeSource)
            )
        }
    }

    private suspend fun buildModelKnowledgeSourceFromUserPlaces(): String {
        val places = placesDao.getAllPlaces().toString()
        return "Below, I will provide you with a list of my saved places." +
                "Then I will tell you what I'm looking for and" +
                " your job is to suggest a up to 3 places for me from the places I provided you." +
                "If you can't find a place that fits my criteria, you should tell me that" +
                " there are no perfect matches but suggest a few places that you think" +
                " are close to what I'm looking for.\n" +
                "Right now the time is: ${LocalDateTime.now()}. " +
                "The city that the user mentions is important in the search criteria." +
                "If I want request for open now, you have to check the openNow field of the place. If nothing is open, ignore this field. \n" +
                "Now here is the list of the users places: $places\n" +
                "The response should include the name of the place, the google maps url from the place object" +
                " from the result object. I want it in the following format: " +
                "{\"gemini_result\":[{\"name\":\"NAME\",\"url\":\"URL\"}]}"
    }

    private fun buildGenerationConfig() = generationConfig {
        temperature = 0.75f
        topK = 10
        topP = 0.7f
        maxOutputTokens = 1000
        responseMimeType = "application/json"
    }
}