package ReactiveExtensions

import scala.concurrent.ExecutionContext.Implicits.global
import rx.lang.scala.observables.{AsyncOnSubscribe, SyncOnSubscribe}
import rx.lang.scala.{Notification, Observable, ObservableExtensions, Subject, Subscription}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

object ObservableExample extends App {

    val obs =  Observable.just("Dog", "Cat", "Mouse")

    obs.subscribe {
      next =>
        println(s"Next is: $next")
      error: Exception => println("Error $error")
      () => println("Completed")
    }

    obs.filterNot(_.contains("M")).subscribe( n => println(s"Next filtered: $n") )

    obs.map(_.length).reduce(_ + _).subscribe(n => println(s"Total length: $n"))

}

object ObservableStream extends App {

  val obs = Observable.interval(1000 millis).take(5)

  obs.subscribe(n => println(s"Emmited: $n"))

  obs.reduce(_ + _).subscribe(n => println(s"Sum of emmited: $n"))


  Thread.sleep(10000)

}

object ObservableCombination extends App {

  val obs = Observable.interval(500 millis).take(10)

  val obs2 = Observable.interval(750 millis).take(5)

  obs.combineLatest(obs2).subscribe(n => println(s"Combination: $n"))

  Thread.sleep(10000)

}

object ObservableAndFutures extends App {

  val f: Future[Int] = Future { 50 }

  val o = Observable.from(f)

  o.subscribe(n => println(s"Value from future: $n"))

}


object SubjectExample extends App {

  val sb = Subject[Int]()

  sb.onNext(10)

  sb.subscribe(n => println(s"Observing: $n"))

  sb.onNext(11)

  sb.onNext(12)

}