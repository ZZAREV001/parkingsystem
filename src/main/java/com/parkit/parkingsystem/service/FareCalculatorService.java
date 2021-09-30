package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;


public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {

        //InputReaderUtil input1 = new InputReaderUtil();

        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        long inHour = ticket.getInTime().getTime() / 1000 / 60;
        long outHour = ticket.getOutTime().getTime() / 1000 / 60;

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
        }

      /*  try {
            if (ticket.getVehicleRegNumber().equals(input1.readVehicleRegistrationNumber())) {
                switch (ticket.getParkingSpot().getParkingType()) {
                    case CAR: {
                        ticket.setPrice(duration * (1 - 0.05) * Fare.CAR_RATE_PER_HOUR);
                        break;
                    }
                    case BIKE: {
                        ticket.setPrice(duration * (1 - 0.05) * Fare.BIKE_RATE_PER_HOUR);
                        break;
                    }
                    default:
                        throw new IllegalArgumentException("Error with fare and type of vehicle");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } */
    }

}