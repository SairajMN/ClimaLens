package com.climaterisk.api.load;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

/**
 * Gatling load test for the risk scores endpoint.
 * <p>
 * Simulates concurrent users querying the current scores API.
 * </p>
 */
public class RiskScoreLoadSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .acceptHeader("application/json")
            .userAgentHeader("Gatling-LoadTest");

    ScenarioBuilder scn = scenario("Risk Score Load Test")
            .exec(http("Get Current Scores")
                    .get("/api/v1/scores/current")
                    .check(status().is(200)))
            .pause(1)
            .exec(http("Get Flood Hotspots")
                    .get("/api/v1/scores/hotspots/flood?limit=5")
                    .check(status().is(200)))
            .pause(1)
            .exec(http("Get Heat Hotspots")
                    .get("/api/v1/scores/hotspots/heat?limit=5")
                    .check(status().is(200)));

    {
        setUp(
                scn.injectOpen(
                        rampUsers(10).during(10), // Warm-up: 10 users over 10s
                        rampUsers(50).during(30), // Load: 50 users over 30s
                        constantUsersPerSec(20).during(60) // Steady: 20 users/sec for 60s
                )).protocols(httpProtocol)
                .assertions(
                        global().responseTime().max().lt(2000), // Max response < 2s
                        global().successfulRequests().percent().gt(95.0) // > 95% success
                );
    }
}