package com.devfest.wellnest.ui.stretch

data class StretchExercise(
    val name: String,
    val durationSeconds: Int,
    val description: String,
    val emoji: String
)

object StretchData {
    val quickBreakExercises = listOf(
        StretchExercise(
            name = "Neck Rolls",
            durationSeconds = 30,
            description = "Gently roll your neck clockwise and counterclockwise",
            emoji = "\uD83E\uDDD8"
        ),
        StretchExercise(
            name = "Shoulder Shrugs",
            durationSeconds = 20,
            description = "Raise shoulders to ears, hold 2 seconds, release",
            emoji = "\uD83D\uDCAA"
        ),
        StretchExercise(
            name = "Wrist Circles",
            durationSeconds = 30,
            description = "Extend arms and rotate wrists in circles",
            emoji = "\uD83E\uDD1C"
        ),
        StretchExercise(
            name = "Seated Spinal Twist",
            durationSeconds = 45,
            description = "Sit tall, twist torso to each side gently",
            emoji = "\uD83D\uDD04"
        ),
        StretchExercise(
            name = "Chest Opener",
            durationSeconds = 30,
            description = "Clasp hands behind back, squeeze shoulder blades",
            emoji = "\uD83D\uDCAB"
        ),
        StretchExercise(
            name = "Eye Rest",
            durationSeconds = 20,
            description = "Close eyes, look away from screen at something distant",
            emoji = "\uD83D\uDC41"
        )
    )

    val reminderIntervals = listOf(30, 45, 60, 90, 120) // minutes
}
