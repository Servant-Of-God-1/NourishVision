// data/ProgressHelper.kt
package com.example.nourishvision.data

import com.example.nourishvision.data.model.FoodLogResponse
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class DailyProgress(
    val date: String,
    val fullDate: String,
    val averageScore: Int,
    val foodCount: Int
)

object ProgressHelper {
    fun getTodayScore(
        foodLogs: List<FoodLogResponse>,
        localScores: Map<Int, Int>
    ): Int {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date())

        val logsToday = foodLogs.filter { log ->
            log.createdAt.startsWith(today)
        }

        val scores = logsToday.mapNotNull { log ->
            localScores[log.id]
        }

        return if (scores.isNotEmpty()) scores.average().toInt() else 0
    }
    fun getWeeklyProgress(
        foodLogs: List<FoodLogResponse>,
        localScores: Map<Int, Int>
    ): List<DailyProgress> {
        val result = mutableListOf<DailyProgress>()
        val calendar = Calendar.getInstance()

        for (i in 6 downTo 0) {
            val dayCalendar = Calendar.getInstance()
            dayCalendar.add(Calendar.DAY_OF_YEAR, -i)

            val fullDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(dayCalendar.time)
            val dayName = SimpleDateFormat("EEE", Locale("id", "ID"))
                .format(dayCalendar.time)

            val logsToday = foodLogs.filter { log ->
                log.createdAt.startsWith(fullDate)
            }

            val scores = logsToday.mapNotNull { log ->
                localScores[log.id]
            }

            val avgScore = if (scores.isNotEmpty()) {
                scores.average().toInt()
            } else 0

            result.add(
                DailyProgress(
                    date = dayName,
                    fullDate = fullDate,
                    averageScore = avgScore,
                    foodCount = logsToday.size
                )
            )
        }

        return result
    }

    fun getWeeklyAverage(weeklyData: List<DailyProgress>): Int {
        val validScores = weeklyData.filter { it.foodCount > 0 }
        return if (validScores.isNotEmpty()) {
            validScores.map { it.averageScore }.average().toInt()
        } else 0
    }

    fun getWeeklyTotalFood(weeklyData: List<DailyProgress>): Int {
        return weeklyData.sumOf { it.foodCount }
    }

    fun getTrend(weeklyData: List<DailyProgress>): String {
        val firstHalf = weeklyData.take(3).filter { it.foodCount > 0 }
        val secondHalf = weeklyData.takeLast(3).filter { it.foodCount > 0 }

        if (firstHalf.isEmpty() || secondHalf.isEmpty()) return "—"

        val firstAvg = firstHalf.map { it.averageScore }.average()
        val secondAvg = secondHalf.map { it.averageScore }.average()

        val diff = secondAvg - firstAvg
        return when {
            diff > 5 -> "↑ Naik"
            diff < -5 -> "↓ Turun"
            else -> "→ Stabil"
        }
    }
}