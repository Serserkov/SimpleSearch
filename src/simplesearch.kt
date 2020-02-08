
import java.util.Scanner
import java.io.File

fun invInd(term: String, input: List<String>): MutableList<Int> {
    val res = mutableListOf<Int>()
    for (i in input.indices) {
        if (input[i].toLowerCase().contains(term)) res.add(i)
    }
    return res
}

fun termsToMap(input: List<String>, terms: List<String>): MutableMap<String, MutableList<Int>> {
    val res = mutableMapOf<String, MutableList<Int>>()
    for (i in terms.indices) {
        res[terms[i]] = invInd(terms[i], input)
    }
    return res
}

fun choiceString(indexList: MutableList<Int>, searchStrat: String, inputSize: Int, reqSize: Int): MutableList<Int> {
    val res = mutableListOf<Int>()
    return when(searchStrat) {
        "ANY" -> indexList.distinct().toMutableList()
        "ALL" -> {
            indexList.groupBy { it }.filterValues { it.size == reqSize }.forEach { res.add(it.value[0])}
            res
        }
        "NONE" -> {
            for (i in 0 until inputSize) if (!indexList.contains(i)) res.add(i)
            res
        }
        else -> res
    }
}

fun main() {
    val scanner = Scanner(System.`in`)
    val input = File("src/text.txt").readLines()
    val terms = input.joinToString(" ").toLowerCase().split(" ").distinct()
    val termsMap = termsToMap(input, terms)
    var exit = false
    do {
        println("=== Menu ===\n1. Find a person\n2. Print all people\n0. Exit")
        when (scanner.nextLine()) {
            "0" -> {
                println("Bye!")
                exit = true
            }
            "2" -> for (i in input) println(i)
            "1" -> {
                println("Select a matching strategy: ALL, ANY, NONE")
                val searhStrat = scanner.nextLine()
                println("Enter a name or email to search all suitable people.")
                val req = scanner.nextLine().toLowerCase().split(" ")
                val indexList = mutableListOf<Int>()
                req.forEach { s ->
                    if (termsMap.containsKey(s.toLowerCase())) termsMap[s]!!.forEach { indexList.add(it) }
                }
                if (indexList.isNotEmpty()) {
                    val res = choiceString(indexList, searhStrat, input.size, req.size)
                    if (res.isNotEmpty()) res.forEach{ println(input[it])} else println("No matching people found.")
                } else println("No matching people found.")
            }
            else -> println("Incorrect option! Try again.")
        }
    } while (!exit)
}
