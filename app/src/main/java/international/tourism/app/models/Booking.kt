package international.tourism.app.models

data class Booking(
    val Id: Int = 0,
    val BookingFor: String = "",
    val HotelName: String = "",
    val BookingDate: String = "",
    val ArrivalDate: String = "",
    val LeavingDate: String= "",
    val Totaldays: Int = 0,
    val TotalRooms: Int = 0,
    val TotalPrice: Int = 0,
    val Status: String = "",
    val BookingIsCancel: Int = 0,
    val PerDayPrice: Int = 0,
    val User_id: Int = 0
)
