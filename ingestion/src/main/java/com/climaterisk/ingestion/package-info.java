/**
 * Data ingestion layer for ClimaLens.
 * <p>
 * Contains dedicated Spring {@code @Service} clients for each external data
 * provider
 * ({@link com.climaterisk.ingestion.client.WeatherApiClient},
 * {@link com.climaterisk.ingestion.client.OpenWeatherClient},
 * {@link com.climaterisk.ingestion.client.TomorrowIoClient},
 * {@link com.climaterisk.ingestion.client.NasaPowerClient},
 * {@link com.climaterisk.ingestion.client.NasaEonetClient}),
 * along with feature engineering services and scheduled ingestion jobs.
 * </p>
 */
package com.climaterisk.ingestion;