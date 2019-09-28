package io.github.thanosfisherman.bayes

import kotlin.math.ln

/**
 * Simple Naive Bayes Classifier in Kotlin
 *
 * @author Thanos Psaridis
 *
 * Helpful References:
 *
 * * https://monkeylearn.com/blog/practical-explanation-naive-bayes-classifier/
 * * https://stats.stackexchange.com/questions/274251/why-do-we-need-laplace-smoothing-in-naive-bayes-while-logarithm-may-resolve-the
 * * https://stats.stackexchange.com/questions/105602/example-of-how-the-log-sum-exp-trick-works-in-naive-bayes
 * * https://github.com/thomasnield/bayes_email_spam
 * * https://github.com/Tradeshift/blayze
 */
class BayesClassifier<C : Any> {

    private var inputs: MutableList<Input<C>> = mutableListOf()
    private val logPriori: Map<C, Double> by lazy { inputs.map { it.category }.groupingBy { it }.eachCount().mapValues { entry -> ln(entry.value / inputs.size.toDouble()) } }
    private val allWordsCount by lazy { inputs.asSequence().flatMap { it.features.asSequence() }.distinct().count() }
    private val featureCounter by lazy {
        val feat = mutableMapOf<C, Int>()
        for (a in inputs)
            feat.merge(a.category, a.features.size, Int::plus)
        feat
    }

    fun train(input: Input<C>) {
        inputs.add(input)
    }

    fun train(inputs: MutableList<Input<C>>) {
        this.inputs = inputs
    }

    fun predict(input: String): Map<C, Double> {


        val mapPredict = mutableMapOf<String, Map<C, Int>>()
        for (w in input.splitWords().distinct().toList()) {
            val categoriesCounter = mutableMapOf<C, Int>()
            for (i in inputs) {
                if (w in i.features)
                    categoriesCounter.merge(i.category, 1, Int::plus)
                else
                    categoriesCounter.merge(i.category, 0, Int::plus)
                mapPredict[w] = categoriesCounter
            }
        }

        val resultsMap = mutableMapOf<C, Double>()
        for (key in mapPredict) {
            for ((cat, count) in key.value) {
                val math = ln((count.toDouble() + 1.0) / (featureCounter.getOrDefault(cat, 0).toDouble() + allWordsCount.toDouble()))
                resultsMap.merge(cat, math, Double::plus)
            }
        }


        val maps = mutableListOf<Map<C, Double>>()
        maps.add(logPriori)
        maps.add(resultsMap)
        return normalize(sumMaps(maps))
    }


    private fun sumMaps(maps: List<Map<C, Double>>): Map<C, Double> {
        val sum = mutableMapOf<C, Double>()
        for (map in maps) {
            for ((key, value) in map) {
                val current = sum.getOrDefault(key, 0.0)
                sum[key] = current + value
            }
        }
        return sum
    }

    private fun normalize(suggestions: Map<C, Double>): Map<C, Double> {
        val max: Double = suggestions.maxBy { it.value }?.value ?: 0.0
        val vals = suggestions.mapValues { Math.exp(it.value - max) }
        val norm = vals.values.sum()
        return vals.mapValues { it.value / norm }
    }

}

data class Input<C>(val text: String, val category: C) {
    val features = text.splitWords().distinct().toList()
}

fun String.splitWords(): Sequence<String> {
    val stopWords = javaClass.getResource("/english-stop-words.txt")
            ?.readText()?.split("\n")?.toSet() ?: setOf()


    return split(Regex("\\s")).asSequence()
            .map { it.replace(Regex("[^A-Za-z]"), "").toLowerCase() }
            .filter { it !in stopWords }
            .filter { it.isNotEmpty() }
}