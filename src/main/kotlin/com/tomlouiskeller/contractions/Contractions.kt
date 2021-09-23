package com.tomlouiskeller.contractions

//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.beans.factory.annotation.Autowired
import java.io.File



const val EXPAND_SLANG_JSON : String = "./src/main/resources/expand/slang.json"

const val EXPAND_SINGLE_CONTRACTIONS_JSON : String = "./src/main/resources/expand/single_contractions.json"
const val EXPAND_DOUBLE_CONTRACTIONS_JSON : String = "./src/main/resources/expand/double_contractions.json"
const val EXPAND_TRIPLE_CONTRACTIONS_JSON : String = "./src/main/resources/expand/triple_contractions.json"

const val EXPAND_SINGLE_NO_APOSTROPHE_CONTRACTIONS_JSON : String = "./src/main/resources/expand/single_no_apostrophe_contractions.json"
const val EXPAND_DOUBLE_NO_APOSTROPHE_CONTRACTIONS_JSON : String = "./src/main/resources/expand/triple_contractions.json"

// We don't use this in the default configuration
const val EXPAND_PARTIAL_CONTRACTIONS_JSON : String = "./src/main/resources/expand/triple_contractions.json"

val CONTRACTIONS_JSON_ORDER :List<String> = listOf(
EXPAND_SLANG_JSON,
EXPAND_DOUBLE_NO_APOSTROPHE_CONTRACTIONS_JSON,
EXPAND_SINGLE_NO_APOSTROPHE_CONTRACTIONS_JSON,
EXPAND_TRIPLE_CONTRACTIONS_JSON,
EXPAND_DOUBLE_CONTRACTIONS_JSON,
EXPAND_SINGLE_CONTRACTIONS_JSON,
)


// TODO: rename `replace` to `replacements`
// TODO: make find a regex
// TODO: make key in replace to regex
// TODO: can we name the map/tuple or something named?
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

        fun getContractionsFromFile(path: String): List<Contraction> {
            val mapper = jacksonObjectMapper()
            mapper.registerKotlinModule()
            val jsonString : String = File(path).readText(Charsets.UTF_8)
            return mapper.readValue<List<Contraction>>(jsonString)
        }
    }

    fun addContractions(path: String) {
        contractions.addAll(getContractionsFromFile(path))
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