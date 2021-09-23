package com.tomlouiskeller.contractions

//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.beans.factory.annotation.Autowired
import java.io.File



const val EXPAND_SLANG_JSON : String = "./src/main/resources/expand/slang.json"

const val EXPAND_DOUBLE_NO_APOSTROPHE_CONTRACTIONS_JSON : String = "./src/main/resources/expand/triple_contractions.json"
const val EXPAND_SINGLE_NO_APOSTROPHE_CONTRACTIONS_JSON : String = "./src/main/resources/expand/single_no_apostrophe_contractions.json"

const val EXPAND_TRIPLE_CONTRACTIONS_JSON : String = "./src/main/resources/expand/triple_contractions.json"
const val EXPAND_DOUBLE_CONTRACTIONS_JSON : String = "./src/main/resources/expand/double_contractions.json"
const val EXPAND_SINGLE_CONTRACTIONS_JSON : String = "./src/main/resources/expand/single_contractions.json"

const val EXPAND_PARTIAL_CONTRACTIONS_JSON : String = "./src/main/resources/expand/triple_contractions.json"

val CONTRACTIONS_JSON_ORDER :List<String> = listOf(
EXPAND_SLANG_JSON,
EXPAND_DOUBLE_NO_APOSTROPHE_CONTRACTIONS_JSON,
EXPAND_SINGLE_NO_APOSTROPHE_CONTRACTIONS_JSON,
EXPAND_TRIPLE_CONTRACTIONS_JSON,
EXPAND_DOUBLE_CONTRACTIONS_JSON,
EXPAND_SINGLE_CONTRACTIONS_JSON,
// We don't use partial contractions in the default configuration
// to add it use `contractionsInclPartials.addContractions(EXPAND_PARTIAL_CONTRACTIONS_JSON)`
// EXPAND_PARTIAL_CONTRACTIONS_JSON,
)

// TODO: remove redundant `replace` entries in JSON files

data class Contraction(val find: Regex, val replace: Map<Regex, String>)

class Contractions @Autowired constructor(
    val objectMapper: ObjectMapper,
) {

    var contractions : MutableList<Contraction> = mutableListOf()

    companion object {
        fun default() : Contractions {
            val contractions = Contractions(ObjectMapper())
            for (contractionsFile in CONTRACTIONS_JSON_ORDER) {
                contractions.addContractions(contractionsFile)
            }
            return contractions
        }

        fun getContractionListFromFile(path: String): List<Contraction> {
            val mapper = jacksonObjectMapper()
            mapper.registerKotlinModule()
            val jsonString : String = File(path).readText(Charsets.UTF_8)
            return mapper.readValue<List<Contraction>>(jsonString)
        }
    }

    fun addContractions(path: String) {
        contractions.addAll(getContractionListFromFile(path))
    }


    fun expand(input: String): String {
        var output = input
        for (contraction in contractions) {
            if (contraction.find.containsMatchIn(output)) {
                for (replace in contraction.replace) {
                    output = replace.key.replace(output, replace.value)
                }
            }
        }
        return output
    }
}