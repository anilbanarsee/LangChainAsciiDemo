package com.abanarsee.langchainascii.controller

import com.abanarsee.langchainascii.service.ai.AiService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Route.tristanController() {

    val aiService by inject<AiService>()

    get("how-old-am-i-in-dog-years") {

        call.respond(aiService.chat(
            "My name is Anil, and I'm 28 years old. " +
                    "How old am I in dog years?"))
    }

    get("wish-me-a-happy-birthday-in-dog-years") {

        call.respond(aiService.chat(
            "My name is Anil, and I'm 28 years old. " +
                    "How old am I in dog years?" +
                    "Please convert your response to ascii text"))
    }

    get("tristan/ascii/convert") {

        call.respond(aiService.chat(
            "Please convert the following word to ascii text: Banana"))
    }

    get("tristan/ascii/how-old-am-i-in-dog-years") {

        call.respond(aiService.chat(
            "I'm 28 years old. " +
                    "How old am I in dog years? e.g 'You are X years old in dog years'" +
                    "Please convert your response to ascii text."))
    }


    get("tristan/ascii/wish-me-a-happy-birthday") {

        call.respond(aiService.chat(
            "My name is Anil, and I'm 28 years old. " +
                    "Please wish me my Xth happy birthday" +
                    "Please convert your response to ascii text"))
    }

}