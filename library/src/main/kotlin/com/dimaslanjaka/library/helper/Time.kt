package com.dimaslanjaka.library.helper

import org.apache.commons.lang3.StringUtils
import java.util.*
import java.util.concurrent.TimeUnit


class Time {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

        }

        const val ONE_SECOND: Long = 1000
        const val SECONDS: Long = 60
        const val ONE_MINUTE = ONE_SECOND * 60
        const val MINUTES: Long = 60
        const val ONE_HOUR = ONE_MINUTE * 60
        const val HOURS: Long = 24
        const val ONE_DAY = ONE_HOUR * 24

        /**
         * converts time (in milliseconds) to human-readable format
         * "<w> days, <x> hours, <y> minutes, (z) seconds"
        </y></x></w> */
        fun ago(duration: Long): String {
            var duration1 = duration
            val res = StringBuilder()
            duration1 = Date().time - duration1
            var temp: Long
            return if (duration1 >= ONE_SECOND) {
                temp = duration1 / ONE_DAY
                if (temp > 0) {
                    duration1 -= temp * ONE_DAY
                    res.append(temp).append(" day").append(if (temp > 1) "s" else "")
                        .append(if (duration1 >= ONE_MINUTE) ", " else "")
                }
                temp = duration1 / ONE_HOUR
                if (temp > 0) {
                    duration1 -= temp * ONE_HOUR
                    res.append(temp).append(" hour").append(if (temp > 1) "s" else "")
                        .append(if (duration1 >= ONE_MINUTE) ", " else "")
                }
                temp = duration1 / ONE_MINUTE
                if (temp > 0) {
                    duration1 -= temp * ONE_MINUTE
                    res.append(temp).append(" minute").append(if (temp > 1) "s" else "")
                }
                if (res.toString() != "" && duration1 >= ONE_SECOND) {
                    res.append(", ")
                }
                temp = duration1 / ONE_SECOND
                if (temp > 0) {
                    res.append(temp).append(" second").append(if (temp > 1) "s" else "")
                }
                res.append(" Ago")
                StringUtils.capitalize(res.toString()).trim()
            } else {
                "0 Second Ago"
            }
        }

        @JvmStatic
        fun secondsAgo(millis: Long): Long {
            val now = Date()
            return TimeUnit.MILLISECONDS.toSeconds(now.time - millis)
        }

        @JvmStatic
        fun minutesAgo(millis: Long): Long {
            val now = Date()
            return TimeUnit.MILLISECONDS.toMinutes(now.time - millis)
        }

        @JvmStatic
        fun hoursAgo(millis: Long): Long {
            val now = Date()
            return TimeUnit.MILLISECONDS.toHours(now.time - millis)
        }

        @JvmStatic
        fun daysAgo(millis: Long): Long {
            val now = Date()
            return TimeUnit.MILLISECONDS.toDays(now.time - millis)
        }
    }
}