Web Service for booking available rooms.

Minimal abilities: no error handling, configuration, logging

API endpoints:
* GET \rooms\bookings?bookingStart=HH:MM:SS&bookingEnd=HH:MM:SS
* POST \rooms\booking?roomId=[1-10]&bookingStart=HH:MM:SS&bookingEnd=HH:MM:SS

- Web Middleware: Akka HTTP
- FRM(ORM) library: Slick
- Database: H2

Docker