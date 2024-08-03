package com.google.mapi.data.gemini

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.google.mapi.data.GOOGLE_GEN_AI_KEY
import com.google.mapi.data.local.PlacesDao
import javax.inject.Inject

class GeminiService @Inject constructor(
    private val placesDao: PlacesDao,
) {

    private val EXAMPLE_PROMPT = "Suggest something with a cozy atmosphere and good coffee."

    suspend fun sendMessage(prompt: String = EXAMPLE_PROMPT): GenerateContentResponse {
        val model = GenerativeModel(
            modelName = "gemini-1.5-pro",
            apiKey = GOOGLE_GEN_AI_KEY,
            generationConfig = buildGenerationConfig(),
            systemInstruction = buildSystemInstruction()
        )

        return model
            .startChat(history = listOf(buildChatHistory()))
            .sendMessage(prompt)
    }

    private suspend fun buildChatHistory(): Content {
        val knowledgeSource = buildModelKnowledgeSourceFromUserPlaces()
        return content("user") { text(knowledgeSource) }
    }

    private suspend fun buildModelKnowledgeSourceFromUserPlaces(): String {
        val places = placesDao.getAllPlaces().toString()
        return "Below, I will provide you with a list of my saved places." +
                "Then I will tell you what I'm looking for and" +
                " your job is to choose a place for me from the places I provided you." +
                "If you can't find a place that fits my criteria, you should tell me that" +
                " there are no perfect matches but suggest a few places that you think" +
                " are close to what I'm looking for.\n" +
                places

    }

    private fun buildSystemInstruction() = content("model") {
        text(
            "Hello! I'm here to help you find the perfect café or restaurant." +
                    " Just tell me what you're looking for and I'll do my best to find it for you."
        )
    }

    private fun buildGenerationConfig() = generationConfig {
        temperature = 0.75f
        topK = 5
        maxOutputTokens = 300
        responseMimeType = "application/json"
    }
}