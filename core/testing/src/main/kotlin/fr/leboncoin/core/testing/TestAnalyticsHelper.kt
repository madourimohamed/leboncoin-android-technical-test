package fr.leboncoin.core.testing

import fr.leboncoin.core.analytics.AnalyticsEvent
import fr.leboncoin.core.analytics.AnalyticsHelper

class TestAnalyticsHelper : AnalyticsHelper {

    private val _events = mutableListOf<AnalyticsEvent>()
    val events: List<AnalyticsEvent> get() = _events

    override fun logEvent(event: AnalyticsEvent) {
        _events += event
    }

    fun hasLogged(event: AnalyticsEvent): Boolean = event in _events
}
