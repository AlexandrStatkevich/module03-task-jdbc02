package by.alst.je.jdbc.main;

import by.alst.je.jdbc.dao.*;
import by.alst.je.jdbc.entity.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcRunner {
    public static void main(String[] args) {

        var aircraftDao = AircraftDao.getInstance();
        var airportDao = AirportDao.getInstance();
        var seatDao = SeatDao.getInstance();
        var flightDao = FlightDao.getInstance();
        var ticketDao = TicketDao.getInstance();

//  Table Aircraft

        System.out.println(aircraftDao.save(new Aircraft(5L, "fgh")));
        System.out.println(aircraftDao.findById(1L));
        System.out.println(aircraftDao.update(new Aircraft(5L, "iooihjij")));
        aircraftDao.findAll().forEach(System.out::println);
        System.out.println(aircraftDao.delete(14L));
        System.out.println(aircraftDao.findById(-1L));
        System.out.println(aircraftDao.delete(-1L));
        System.out.println(aircraftDao.save(null));
        System.out.println(aircraftDao.update(null));

//  Table Airport

//        System.out.println(airportDao.save(new Airport(AirportCode.GLM, Country.Беларусь, City.Гомель)));
//        System.out.println(airportDao.findById(AirportCode.BSL));
//        airportDao.findAll().forEach(System.out::println);
//        System.out.println(airportDao.update(new Airport(AirportCode.GML, Country.Беларусь, City.Гомель)));
//        System.out.println(airportDao.findById(AirportCode.GML));
//        System.out.println(airportDao.delete(AirportCode.GML));
//        System.out.println(airportDao.findById(null));
//        System.out.println(airportDao.save(null));
//        System.out.println(airportDao.update(null));
//        System.out.println(airportDao.delete(null));


//  Table Seat

//        Seat seat1 = new Seat(5, "A1");
//        Seat seat2 = new Seat(5, "A2");
//        Seat seat3 = new Seat(5, "B1");
//        Seat seat4 = new Seat(5, "B2");
//        Seat seat5 = new Seat(5, "C1");
//        Seat seat6 = new Seat(5, "C2");
//        Seat seat7 = new Seat(5, "D1");
//        Seat seat8 = new Seat(5, "D2");
//        List<Seat> list = new ArrayList<>();
//        list.add(seat1);
//        list.add(seat2);
//        list.add(seat3);
//        list.add(seat4);
//        list.add(seat5);
//        list.add(seat6);
//        list.add(seat7);
//        list.add(seat8);
//        System.out.println(seatDao.save(list));
//        List<Seat> seats = seatDao.findById(5).orElse(null);
//        seatDao.findById(5).get().forEach(System.out::println);
//        Seat seat9 = new Seat(5, "B22");
//        Seat seat10 = new Seat(5, "B34");
//        seats.add(seat9);
//        seats.add(seat10);
//        System.out.println(seatDao.update(seats));
//        System.out.println(seatDao.update(null));
//        System.out.println(seatDao.save(null));
//        System.out.println(seatDao.delete(5));
//        System.out.println(seatDao.delete(-1));
//        seatDao.findById(-1).get().forEach(System.out::println);

//  Table Flight

//        Flight flight = flightDao.findById(4L).orElse(null);
//        System.out.println(flight);
//        flight.setStatus(FlightStatus.CANCELLED);
//        System.out.println(flightDao.update(flight));
//        flight.setAircraftId(5);
//        flight.setFlightNo("qwerty567");
//        flight.setStatus(FlightStatus.DEPARTED);
//        System.out.println(flightDao.save(flight));
//        flightDao.findAll().forEach(System.out::println);
//        System.out.println(flightDao.delete(flight.getId()));
//        System.out.println(flightDao.update(null));
//        System.out.println(flightDao.save(null));
//        System.out.println(flightDao.findById(-1L));
//        System.out.println(flightDao.delete(-1L));

//  Table Ticket

//        Ticket ticket = ticketDao.findById(10L).orElse(null);
//        System.out.println(ticket);
//        ticket.setCoast(BigDecimal.valueOf(300));
//        System.out.println(ticketDao.update(ticket));
//        ticket.setSeatNo("D15");
//        ticket.setPassengerName("John Nhoj");
//        ticket.setFlightId(5L);
//        System.out.println(ticketDao.save(ticket));
//        ticketDao.findAll().forEach(System.out::println);
//        System.out.println(ticketDao.delete(ticket.getId()));
//        System.out.println(ticketDao.update(null));
//        System.out.println(ticketDao.save(null));
//        System.out.println(ticketDao.findById(-1L));
//        System.out.println(ticketDao.delete(-1L));
    }
}
