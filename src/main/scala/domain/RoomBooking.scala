package domain

import java.sql.Time

// This is a pure domain logic class and should not have any high level dependencies and details

case class RoomBooking(
    roomId: Long,
    bookingStart: Time,
    bookingEnd: Time
)
