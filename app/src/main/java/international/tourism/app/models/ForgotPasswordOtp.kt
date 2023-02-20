package international.tourism.app.models

data class ForgotPasswordOtp(
    val otp: String,
    val email: String,
    val password: String
)
