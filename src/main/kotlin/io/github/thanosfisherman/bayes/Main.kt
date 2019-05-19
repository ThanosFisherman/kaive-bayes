package io.github.thanosfisherman.bayes

fun main() {

    //EXAMPLE 1: FILTERING SPORTS-RELATED TEXT

    var bae = BayesClassifier<String>()

    bae.train(Input("A great game", "sports"))
    bae.train(Input("The election was over", "notSports"))

    bae.train(Input("Very clean match", "sports"))
    bae.train(Input("A clean but forgettable game", "sports"))
    bae.train(Input("It was a close election", "notSports"))

    val message1 = "A very close game"
    var map = bae.predict(message1)
    println("RESULTS OF TEXT: $message1")
    println("------------------------------------------------------------------")
    println("Is about sports " + map["sports"])
    println("Is NOT about sports " + map["notSports"])
    println()

    //EXAMPLE 2 SPAM MAIL FILTERING

    val emailPopulation = listOf(
            Input("Hey there! I thought you might find this interesting. Click here.", "isSpam"),
            Input("Get viagra for a discount as much as 90%", "isSpam"),
            Input("Viagra prescription for less", "isSpam"),
            Input("Even better than Viagra, try this new prescription drug", "isSpam"),

            Input("Hey, I left my phone at home. Email me if you need anything. I'll be in a meeting for the afternoon.", "isNotSpam"),
            Input("Please see attachment for notes on today's meeting. Interesting findings on your market research.", "isNotSpam"),
            Input("An item on your Amazon wish list received a discount", "isNotSpam"),
            Input("Your prescription drug order is ready", "isNotSpam"),
            Input("Your Amazon account password has been reset", "isNotSpam"),
            Input("Your Amazon order", "isNotSpam")
    )

    bae = BayesClassifier()

    bae.train(emailPopulation.toMutableList())

    val message2 = "discount viagra wholesale, hurry while this offer lasts"
    map = bae.predict(message2)
    println("RESULTS OF TEXT: $message2")
    println("------------------------------------------------------------------")
    println("IS SPAM: " + map["isSpam"])
    println("IS NOT SPAM: " + map["isNotSpam"])
    println()

    val message3 = "interesting meeting on amazon cloud services discount program"
    map = bae.predict(message3)
    println("RESULTS OF TEXT: $message3")
    println("------------------------------------------------------------------")
    println("IS SPAM: " + map["isSpam"])
    println("IS NOT SPAM: " + map["isNotSpam"])
    println()
}
