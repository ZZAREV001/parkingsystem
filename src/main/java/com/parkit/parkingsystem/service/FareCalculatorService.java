package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;

import static com.parkit.parkingsystem.constants.ParkingType.BIKE;
import static com.parkit.parkingsystem.constants.ParkingType.CAR;


public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {

        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        long inHour = ticket.getInTime().getTime() / 1000 / 60;    // Divide by 1000 ms and 60 s in order to avoid errors when calculating duration
        long outHour = ticket.getOutTime().getTime() / 1000 / 60;
        TicketDAO ticketDAO = new TicketDAO();

        //TODO: Some tests are failing here. Need to check if this logic is correct (remark: do not create new methods, all code should be here)
        long duration = outHour - inHour;

        if (duration <= 30) {
            ticket.setPrice(0);
        } else {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR / 60);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR / 60);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
            }
            if (ticketDAO.isRecurrentUser(ticket.getVehicleRegNumber())) {
                // calculate price of the ticket with a reduction
                ticket.setPrice(ticket.getPrice() * 0.95);
            }
        }
    }

}