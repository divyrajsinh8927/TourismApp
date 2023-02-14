package international.tourism.app.models

data class User(
    val Id: Int = 0,
    val Name: String = "",
    val MobileNumber: Int = 0,
    val UserType: String = "U",
    val Email: String = "",
    val PasswordHash: String = "",
    val Status: Int = 0,
    val UserIsDelete: Int = 0,
    val FirebaseToken: String = ""
    )