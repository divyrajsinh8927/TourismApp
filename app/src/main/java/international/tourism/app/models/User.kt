package international.tourism.app.models

data class User(
    var Id: Int = 0,
    var Name: String = "",
    var MobileNumber: Int = 0,
    var UserType: String = "U",
    var Email: String = "",
    var PasswordHash: String = "",
    var Status: Int = 0,
    var UserIsDelete: Int = 0
    )