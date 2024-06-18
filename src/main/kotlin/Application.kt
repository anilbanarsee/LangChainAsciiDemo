import com.abanarsee.langchainascii.service.ai.AiService
import com.abanarsee.langchainascii.service.tools.AsciiTools
import com.abanarsee.shared.plugins.httpClient
import com.abanarsee.shared.model.Secrets
import com.abanarsee.langchainascii.plugins.configureRouting
import com.abanarsee.shared.plugins.configureSecurity
import com.abanarsee.shared.plugins.configureSerialization
import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.openai.OpenAiChatModel
import dev.langchain4j.service.AiServices
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.serialization.json.Json
import org.koin.ktor.plugin.Koin
import java.nio.file.Paths
import kotlin.io.path.readText


fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSecurity()
    configureSerialization()
    configureRouting()

    val secrets = Json.decodeFromString<Secrets>(Paths.get("keys/keys.json").readText())

    val model: ChatLanguageModel = OpenAiChatModel.builder()
        .apiKey(secrets.chatgpt)
        .logRequests(false)
        .build()

    val appModule = org.koin.dsl.module {
        single { httpClient() }
        single {
            AiServices.builder(AiService::class.java)
            .tools(AsciiTools())
            .chatLanguageModel(model)
            .chatMemory(MessageWindowChatMemory.withMaxMessages(100))
            .build()
        }
    }

    install(Koin) {
        modules(appModule)
    }
}
