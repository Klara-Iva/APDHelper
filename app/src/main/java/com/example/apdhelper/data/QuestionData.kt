package com.example.apdhelper.data

import com.example.apdhelper.model.Question

val labelEncoders = mapOf(
    "Gender" to mapOf("Female" to 0f, "Male" to 1f),
    "Family History" to mapOf("No" to 0f, "Yes" to 1f),
    "Personal History" to mapOf("No" to 0f, "Yes" to 1f),
    "Current Stressors" to mapOf("High" to 0f, "Low" to 1f, "Moderate" to 2f),
    "Symptoms" to mapOf(
        "Chest pain" to 0f,
        "Dizziness" to 1f,
        "Fear of losing control" to 2f,
        "Panic attacks" to 3f,
        "Shortness of breath" to 4f
    ),
    "Severity" to mapOf("Mild" to 0f, "Moderate" to 1f, "Severe" to 2f),
    "Impact on Life" to mapOf("Mild" to 0f, "Moderate" to 1f, "Significant" to 2f),
    "Demographics" to mapOf("Rural" to 0f, "Urban" to 1f),
    "Medical History" to mapOf(
        "Asthma" to 0f, "Diabetes" to 1f, "Heart disease" to 2f, "Unknown" to 3f
    ),
    "Psychiatric History" to mapOf(
        "Anxiety disorder" to 0f,
        "Bipolar disorder" to 1f,
        "Depressive disorder" to 2f,
        "Unknown" to 3f
    ),
    "Substance Use" to mapOf("Alcohol" to 0f, "Drugs" to 1f, "Unknown" to 2f),
    "Coping Mechanisms" to mapOf(
        "Exercise" to 0f, "Meditation" to 1f, "Seeking therapy" to 2f, "Socializing" to 3f
    ),
    "Social Support" to mapOf("High" to 0f, "Low" to 1f, "Moderate" to 2f),
    "Lifestyle Factors" to mapOf("Diet" to 0f, "Exercise" to 1f, "Sleep quality" to 2f)
)

val featureMeans = mapOf(
    "Age" to 41.4543f,
    "Gender" to 0.50052f,
    "Family History" to 0.50042f,
    "Personal History" to 0.4979f,
    "Current Stressors" to 0.99853f,
    "Symptoms" to 1.99758f,
    "Severity" to 1.00145f,
    "Impact on Life" to 0.99763f,
    "Demographics" to 0.49913f,
    "Medical History" to 1.50579f,
    "Psychiatric History" to 1.49813f,
    "Substance Use" to 0.99929f,
    "Coping Mechanisms" to 1.50478f,
    "Social Support" to 0.999f,
    "Lifestyle Factors" to 0.99741f
)

val featureStds = mapOf(
    "Age" to 13.83913478f,
    "Gender" to 0.49999973f,
    "Family History" to 0.49999982f,
    "Personal History" to 0.49999559f,
    "Current Stressors" to 0.81603176f,
    "Symptoms" to 1.41169903f,
    "Severity" to 0.81777008f,
    "Impact on Life" to 0.81649518f,
    "Demographics" to 0.49999924f,
    "Medical History" to 1.11741956f,
    "Psychiatric History" to 1.11754933f,
    "Substance Use" to 0.81742859f,
    "Coping Mechanisms" to 1.1183636f,
    "Social Support" to 0.81734876f,
    "Lifestyle Factors" to 0.81487624f
)

fun getQuestions(): List<Question> = listOf(

    Question("How old are you? 👶", "Age", "Age", isNumeric = true), Question(
        "How do you identify? 🌈",
        "Gender",
        "Gender",
        options = listOf("Female", "Male"),
        displayOptions = listOf("I’m a woman 👧", "I’m a man 👦")
    ), Question(
        "Any family history of anxiety? 🏟️",
        "Family History",
        "Family History",
        options = listOf("No", "Yes"),
        displayOptions = listOf("Nope, none that I know 😊", "Yes, someone in my family")
    ), Question(
        "Have you experienced anxiety before? 😓",
        "Personal History",
        "Personal History",
        options = listOf("No", "Yes"),
        displayOptions = listOf("Never really", "Yes, I have")
    ), Question(
        "How intense is your current stress? 🌧️",
        "Current Stressors",
        "Current Stressors",
        options = listOf("High", "Low", "Moderate"),
        displayOptions = listOf("Very intense 😬", "Pretty calm 🌼", "Somewhere in between")
    ), Question(
        "Which of these symptoms have you noticed lately? 🤔",
        "Symptoms",
        "Symptoms",
        options = listOf(
            "Chest pain",
            "Dizziness",
            "Fear of losing control",
            "Panic attacks",
            "Shortness of breath"
        ),
        displayOptions = listOf(
            "Tight chest", "Feeling dizzy", "Fear of losing it", "Panic episodes", "Hard to breathe"
        )
    ), Question(
        "How would you rate the severity of your symptoms? 🌌",
        "Severity",
        "Severity",
        options = listOf("Mild", "Moderate", "Severe"),
        displayOptions = listOf("Mild 😐", "Moderate 😕", "Severe 😫")
    ), Question(
        "How much do your symptoms affect your daily life? 📚",
        "Impact on Life",
        "Impact on Life",
        options = listOf("Mild", "Moderate", "Significant"),
        displayOptions = listOf("Barely noticeable", "Manageable", "Quite a lot")
    ), Question(
        "Where do you live? 🌆",
        "Demographics",
        "Demographics",
        options = listOf("Rural", "Urban"),
        displayOptions = listOf("Countryside 🌿", "City life 🌇")
    ), Question(
        "Any history of physical health conditions? 🏥",
        "Medical History",
        "Medical History",
        options = listOf("Asthma", "Diabetes", "Heart disease", "Unknown"),
        displayOptions = listOf("Asthma", "Diabetes", "Heart condition", "Not sure")
    ), Question(
        "Have you been diagnosed with any mental health conditions? 🤦",
        "Psychiatric History",
        "Psychiatric History",
        options = listOf("Anxiety disorder", "Bipolar disorder", "Depressive disorder", "Unknown"),
        displayOptions = listOf("Anxiety", "Bipolar", "Depression", "Not diagnosed")
    ), Question(
        "Do you use any substances? 🍻",
        "Substance Use",
        "Substance Use",
        options = listOf("Alcohol", "Drugs", "Unknown"),
        displayOptions = listOf("Alcohol", "Other drugs", "Neither")
    ), Question(
        "How do you usually cope with stress? 🧘",
        "Coping Mechanisms",
        "Coping Mechanisms",
        options = listOf("Exercise", "Meditation", "Seeking therapy", "Socializing"),
        displayOptions = listOf(
            "I workout 🏋", "I meditate 😴", "I talk to a therapist 💬", "I hang out with friends 🌞"
        )
    ), Question(
        "How would you rate your social support? 👬",
        "Social Support",
        "Social Support",
        options = listOf("Low", "Moderate", "High"),
        displayOptions = listOf("Not really", "Somewhat", "Very supportive 💕")
    ), Question(
        "What’s your current lifestyle like? 🥗",
        "Lifestyle Factors",
        "Lifestyle Factors",
        options = listOf("Diet", "Exercise", "Sleep quality"),
        displayOptions = listOf("Focused on eating well", "I stay active", "Sleep matters to me")
    )
)

