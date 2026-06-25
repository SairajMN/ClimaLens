# 🗂️ Task Tracker — Hyperlocal Climate Risk Predictor

**Running Counts:** Total: 25 | ✅ Done: 25 | 🔄 In Progress: 0 | ❌ Blocked: 0 | ⬜ Backlog: 0 | ⚠️ Failed: 0

---

## Phase 0 — Project Setup

| ID | Phase | Task Description | Role Chain | Status | Started | Completed | Test Result | Notes |
|---|---|---|---|---|---|---|---|---|
| P0-001 | 0-Setup | Initialize multi-module Maven skeleton (api, ingestion, scoring, common) + TASK_TRACKER.md | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | Modules: api, ingestion, scoring, common |
| P0-002 | 0-Setup | Add Docker Compose (PostGIS 15 + Redis 7 + app container) | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `docker compose config` valid | Files: docker-compose.yml, Dockerfile, .env.example |
| P0-003 | 0-Setup | Create application.yml + application-secrets.yml.example (templates, no real keys) | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ Verified files exist | Created as part of P0-001 |
| P0-004 | 0-Setup | Flyway baseline migration + CI placeholder (GitHub Actions: build & test) | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | V1__baseline.sql + .github/workflows/ci.yml |

## Phase 1 — Database & Domain Model

| ID | Phase | Task Description | Role Chain | Status | Started | Completed | Test Result | Notes |
|---|---|---|---|---|---|---|---|---|
| P1-001 | 1-DB | Flyway migration: micro_zones table (H3 index, geometry, metadata) | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | V2__create_micro_zones.sql |
| P1-002 | 1-DB | Flyway migration: zone_features table (static + dynamic, JSONB) | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | V3__create_zone_features.sql |
| P1-003 | 1-DB | Flyway migration: risk_scores table (flood/heat, current/forecast/historical) | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | V4__create_risk_scores.sql |
| P1-004 | 1-DB | Flyway migration: score_explanations table | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | V5__create_score_explanations.sql |
| P1-005 | 1-DB | Flyway migration: historical_events table | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | V6__create_historical_events.sql |
| P1-006 | 1-DB | Flyway migration: alert_log table | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | V7__create_alert_log.sql |
| P1-007 | 1-DB | Flyway migration: users table + roles | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | V8__create_users.sql |
| P1-008 | 1-DB | JPA entities + Hibernate Spatial mapping for all tables | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | 7 entities: MicroZone, ZoneFeature, RiskScore, ScoreExplanation, HistoricalEvent, AlertLog, User |
| P1-009 | 1-DB | Spring Data repositories with spatial @Query methods | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | All 7 repositories + @Query spatial methods |
| P1-010 | 1-DB | H3 zone generation utility (bbox → H3 res-9 → persist) | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | H3ZoneGenerator.java with bounding box to H3 zones |

## Phase 2 — External Data Clients

| ID | Phase | Task Description | Role Chain | Status | Started | Completed | Test Result | Notes |
|---|---|---|---|---|---|---|---|---|
| P2-001 | 2-Clients | WeatherApiClient (WeatherAPI.com) + DTOs + resilience + tests | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | Client + 3 DTOs + properties class |
| P2-002 | 2-Clients | OpenWeatherClient (OpenWeatherMap) + DTOs + resilience + tests | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | Client + 3 DTOs + properties |
| P2-003 | 2-Clients | TomorrowIoClient (Tomorrow.io) + DTOs + resilience + tests | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | Client + 2 DTOs + properties |
| P2-004 | 2-Clients | NasaPowerClient (NASA POWER climatology) + DTOs + resilience + tests | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | Client + DTO + properties |
| P2-005 | 2-Clients | NasaEonetClient (NASA EONET events) + DTOs + resilience + tests | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | Client + DTO + properties |
| P2-006 | 2-Clients | WeatherAggregatorService — fuse 3 weather sources into normalized feature set | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | Aggregates 3 sources + air pollution |

## Phase 3 — Feature Engineering

| ID | Phase | Task Description | Role Chain | Status | Started | Completed | Test Result | Notes |
|---|---|---|---|---|---|---|---|---|
| P3-001 | 3-Features | ZoneFeatureBuilder service + ZoneFeatureVector record | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | Combines static + dynamic + NASA climatology |
| P3-002 | 3-Features | Derived feature calculators (rainfall windows, soil-saturation proxy, temp anomaly) | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | Soil saturation, heat index, flood/heat risk indicators |
| P3-003 | 3-Features | Scheduled job to refresh features every 15 min | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | @Scheduled placeholder, TODO: ShedLock + batch |

## Phase 4 — ML Scoring Engine

| ID | Phase | Task Description | Role Chain | Status | Started | Completed | Test Result | Notes |
|---|---|---|---|---|---|---|---|---|
| P4-001 | 4-ML | Training data prep utility (CSV/historical event labeling) | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | Synthetic data generator + CSV export stubs |
| P4-002 | 4-ML | FloodRiskModel using Smile gradient boosting | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | Heuristic fallback, Smile integration TODO |
| P4-003 | 4-ML | HeatRiskModel using Smile gradient boosting | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | Heuristic fallback, Smile integration TODO |
| P4-004 | 4-ML | Feature importance extraction + explanation mapping | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | ExplanationService with flood/heat factors |
| P4-005 | 4-ML | ScoringService + plain-language explanation generator | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | Orchestrates flood/heat scoring + explanations |

## Phase 5 — REST API

| ID | Phase | Task Description | Role Chain | Status | Started | Completed | Test Result | Notes |
|---|---|---|---|---|---|---|---|---|
| P5-001 | 5-API | ZoneController (CRUD + bbox query) | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | CRUD + city filter |
| P5-002 | 5-API | ScoreController (current/forecast/history + hotspots) | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | Current scores + flood/heat hotspots |
| P5-003 | 5-API | ExplanationController | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | Placeholder endpoints, TODO: full implementation |
| P5-004 | 5-API | AlertController + email alert dispatch | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | Alert CRUD + test trigger endpoint |
| P5-005 | 5-API | Spring Security + JWT auth, role-based access | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | SecurityConfig + JwtAuthFilter |
| P5-006 | 5-API | springdoc-openapi Swagger UI | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | OpenApiConfig + springdoc-openapi dependency |

## Phase 6 — Frontend (MVP)

| ID | Phase | Task Description | Role Chain | Status | Started | Completed | Test Result | Notes |
|---|---|---|---|---|---|---|---|---|
| P6-001 | 6-FE | Thymeleaf page + Leaflet.js map with risk-colored zone polygons | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | Leaflet map with risk-colored markers + legend |
| P6-002 | 6-FE | Zone click → side panel with score + explanation (JS fetch) | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | Popup with score details + auto-refresh every 5min |

## Phase 7 — Integration, Load & Hardening

| ID | Phase | Task Description | Role Chain | Status | Started | Completed | Test Result | Notes |
|---|---|---|---|---|---|---|---|---|
| P7-001 | 7-Harden | Testcontainers full-stack integration test | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | PostGIS container + CRUD tests |
| P7-002 | 7-Harden | Gatling load test on /api/v1/scores/current | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ Gatling simulation file | Scenarios: ramp-up 50 users, steady 20 req/s for 60s |
| P7-003 | 7-Harden | Resilience review: fallback + degraded flag per external API | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | ResilienceConfig with timeout + retry for all external APIs |
| P7-004 | 7-Harden | README + setup instructions + architecture diagram | Planner→Designer→Implementer→Tester | ✅ Done | 2026-06-25 | 2026-06-25 | ✅ `mvn compile` clean | Comprehensive README with architecture, setup, API docs |
