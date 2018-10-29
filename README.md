Kaive Bayes
--------------

Sorry about the ridiculous name, this is a very simple Naive Bayes classifier written in Kotlin.

### Techniques used

In order to be competitive, Kaive Bayes leverages the following techniques:

- Laplace Smoothing for eliminating zero probabilities
- log-sum trick to prevent number underflows
- Normalization
- Stopword purging

Serialization is to be added soon along with various optimization enhancements.

### Usage

```kotlin
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
``` 

For more examples see the [Main](/src/main/kotlin/io/github/thanosfisherman/bayes/Main.kt) function

### Add it to your project 

You may find kaive-bayes under jcenter. [ ![Download](https://api.bintray.com/packages/thanosfisherman/maven/kaive-bayes/images/download.svg?version=kaive-bayes1.0.0) ](https://bintray.com/thanosfisherman/maven/kaive-bayes/kaive-bayes1.0.0/link)


**Gradle dependency

```groovy
compile 'io.github.thanosfisherman:kaive-bayes:<latest_version_here>'
```

**Maven dependency

```xml
<dependency>
  <groupId>io.github.thanosfisherman</groupId>
  <artifactId>kaive-bayes</artifactId>
  <version>latest_version_here</version>
  <type>pom</type>
</dependency>
```
