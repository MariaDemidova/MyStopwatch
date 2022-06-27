package com.example.mystopwatch.model


const val ONE_THOUSAND = 1000
const val SIXTY = 60
class TimestampMillisecondsFormatter {
    fun format(timestamp: Long): String {
        val millisecondsFormatted = (timestamp % ONE_THOUSAND).pad(3)
        val seconds = timestamp / ONE_THOUSAND
        val secondsFormatted = (seconds % SIXTY).pad(2)
        val minutes = seconds / SIXTY
        val minutesFormatted = (minutes % SIXTY).pad(2)
        val hours = minutes / SIXTY
        return if (hours > 0) {
            val hoursFormatted = (minutes / SIXTY).pad(2)
            "$hoursFormatted:$minutesFormatted:$secondsFormatted"
        } else {
            "$minutesFormatted:$secondsFormatted:$millisecondsFormatted"
        }
    }

    private fun Long.pad(desiredLength: Int) = this.toString().padStart(desiredLength, '0')

    companion object {
        const val DEFAULT_TIME = "00:00:000"
    }
}