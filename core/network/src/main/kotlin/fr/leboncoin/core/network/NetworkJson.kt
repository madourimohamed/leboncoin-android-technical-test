package fr.leboncoin.core.network

import kotlinx.serialization.json.Json

/**
 * Lenient parser: new fields added by the backend must not break old app versions,
 * and `null` values for non-nullable fields with defaults fall back to the default.
 */
internal val NetworkJson = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}
