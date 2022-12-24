package international.tourism.app.models

data class Booking(
    val id: Int = 0,
    val userId: Int = 0,
    val bookFor: String = "",
    val hotelId: Int = 0,
    val bookingDate: String = "",
    val arrivalDate: String = "",
    val leavingDate: String= "",
    val totalDays: Int = 0,
    val totalRooms: Int = 0,
    val totalPrise: Int = 0,
    val status: Int = 0,
    val bookingIsCancel: Int = 0
)
