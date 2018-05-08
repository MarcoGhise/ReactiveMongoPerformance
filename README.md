### Reactive Mongo Performance ###

Two solutions wrote in order to compare the performance of REST MongoDb with reactive and non-reactive solution.
Based on Rodrigo Chaves solution's (https://itnext.io/reactive-microservices-with-spring-5-95c5f8cd03d0) I wrote the non-reactive version of the solution `account-servlet`.  

#### account-reactive ####

Reactive solution written by Rodrigo Chaves available also at https://github.com/LINKIT-Group/spring-rest-reactive.

#### account-servlet ####

A non-reactive adaptation of Rodrigo's solution

#### Performance Test ####

I tested the two solutions using gatling with this configuration.

Number of Users: 100
Number of requests per seconds: 100 req/sec
Duration of test: 1 minute

```scala
import java.net.HttpURLConnection

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._

import scala.util.Random

import scala.concurrent.duration._

class Mongo extends Simulation {

  val rampUpTimeSecs = 5
  val testTimeSecs = 60
  val noOfUsers = 100
  val noOfRequestPerSeconds = 100

  val baseURL = "http://localhost:8080"
  val accountResourcePath = "/accounts"

  object LoadAccountByCurrencies {
     
    val all = exec(http("LABAccounts")
      .get(accountResourcePath + "/")
      .check(status.is(HttpURLConnection.HTTP_OK)))   
  }

  val httpProtocol = http
    .baseURL(baseURL)
    .acceptHeader("application/json")
    .userAgentHeader("Gatling")

    val testScenario = scenario("LoadTest")
    .during(testTimeSecs) {
      exec(
        LoadAccountByCurrencies.all
      )
    }

  setUp(
    testScenario
      .inject(atOnceUsers(noOfUsers)))
    .throttle(
      reachRps(noOfRequestPerSeconds) in (rampUpTimeSecs seconds),
      holdFor(testTimeSecs seconds))
    .protocols(httpProtocol)

}
```
  
I invoked only the URL `GET http://localhost:8080/accounts` which gets all the records in the collection.

My system configuration provides 4 cores cpu.

#### Result ####

##### account-servlet #####
```text
================================================================================
---- Global Information --------------------------------------------------------
> request count                                       1431 (OK=1431   KO=0     )
> min response time                                    142 (OK=142    KO=-     )
> max response time                                   9099 (OK=9099   KO=-     )
> mean response time                                  4200 (OK=4200   KO=-     )
> std deviation                                       2933 (OK=2933   KO=-     )
> response time 50th percentile                       5783 (OK=5783   KO=-     )
> response time 75th percentile                       6439 (OK=6439   KO=-     )
> response time 95th percentile                       7533 (OK=7533   KO=-     )
> response time 99th percentile                       8863 (OK=8863   KO=-     )
> mean requests/sec                                 22.359 (OK=22.359 KO=-     )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                           460 ( 32%)
> 800 ms < t < 1200 ms                                   6 (  0%)
> t > 1200 ms                                          965 ( 67%)
> failed                                                 0 (  0%)
================================================================================
```

##### account-reactive #####
```text
================================================================================
---- Global Information --------------------------------------------------------
> request count                                       1018 (OK=1018   KO=0     )
> min response time                                    213 (OK=213    KO=-     )
> max response time                                  23257 (OK=23257  KO=-     )
> mean response time                                  5943 (OK=5943   KO=-     )
> std deviation                                       3190 (OK=3190   KO=-     )
> response time 50th percentile                       5631 (OK=5631   KO=-     )
> response time 75th percentile                       7998 (OK=7998   KO=-     )
> response time 95th percentile                      11423 (OK=11423  KO=-     )
> response time 99th percentile                      14498 (OK=14498  KO=-     )
> mean requests/sec                                 15.662 (OK=15.662 KO=-     )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                            25 (  2%)
> 800 ms < t < 1200 ms                                  25 (  2%)
> t > 1200 ms                                          968 ( 95%)
> failed                                                 0 (  0%)
================================================================================
```

#### Conclusion ####

* Reactive responses average result are slower than the non-reactive one. 

* The throughput is about 30% faster in non-reactive solution.

* Making the test with only one request, reactive solution is faster than non-reactive (127ms and 260ms).

* Also, reactive solution opens a huge number of connections to MongoDb. 

Considering the evidence of the above test, I guess `org.springframework.data.mongodb.repository.ReactiveMongoRepository` has a noticeable scalability issue.

I hope someone could disprove my conclusion and let me know why I got this result! 

