package com.abanarsee.langchainascii.service.ai

import dev.langchain4j.service.*;

interface AiService {

    @SystemMessage
    fun chat(userMessage: String): String
}