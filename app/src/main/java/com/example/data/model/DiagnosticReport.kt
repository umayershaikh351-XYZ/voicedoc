package com.example.data.model

data class DiagnosticReport(
  val id: String,
  val userQuery: String,
  val title: String,
  val simpleExplanation: String,
  val rootCause: String,
  val severity: ActionSeverity,
  val healthScoreBefore: Int,
  val healthScoreAfter: Int,
  val recommendedActions: List<AutoFixAction>,
  val timestamp: Long = System.currentTimeMillis()
)
