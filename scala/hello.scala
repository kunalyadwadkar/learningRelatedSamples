println("Hello, world, from scala!")

println("Hello, " + args(0) + "!")

println("Printing all args one by one")
args.foreach(arg => println(arg))

val greetStrings = ("Hello", " , ", " World!\n")

print(greetStrings._1)
print(greetStrings._2)
print(greetStrings._3)

val greeter = new FriendlyGreeting

println("We have an instance! ")
println(greeter.greet())

greeter.add(5)

greeter.printSum()

greeter.add(11)

greeter.printSum()

import scala.collection.mutable

class FriendlyGreeting {
  private var sum = 0

  def add(b: Int) = { sum += b}

  def greet() = { "Hello, world! from a function."}

  def printSum() = { println(sum)}

}

var jetSet = Set("with parameter inference")
jetSet += "Learjet"


println("Jetset contains 'Cessna' ? " + jetSet.contains("Cessna"))

print("That set contained: ")
println(jetSet)

var withoutInference = mutable.Set[String]()

withoutInference += " cool, huh? "

withoutInference += "Yeah, totally "
println(withoutInference)

withoutInference = mutable.Set("totally different set")
