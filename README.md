Web Service for booking available rooms.

Minimal abilities: no error handling, configuration, logging. General web service testing added only.

API endpoints:
* GET \rooms\bookings?bookingStart=HH:MM:SS&bookingEnd=HH:MM:SS
* POST \rooms\booking?roomId=[1-10]&bookingStart=HH:MM:SS&bookingEnd=HH:MM:SS

- Web Middleware: Akka HTTP
- FRM(ORM) library: Slick
- Database: H2

To run in docker:

In SBT shell: ;clean;reload;docker:stage
Then: docker-compose up --build
The service in the container is available at port 80.
