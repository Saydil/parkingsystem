package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	TicketDAO ticketDAO = new TicketDAO();

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		int inHour = ticket.getInTime().getDay() * 24 * 60 + ticket.getInTime().getHours() * 60
				+ ticket.getInTime().getMinutes();
		int outHour = ticket.getOutTime().getDay() * 24 * 60 + ticket.getOutTime().getHours() * 60
				+ ticket.getOutTime().getMinutes();

		// TODO: Some tests are failing here. Need to check if this logic is correct
		double duration = (outHour - inHour) / 60.0;

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			if (duration <= 0.5) {
				ticket.setPrice(0);
				break;
			} else {
				ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
				break;
			}
		}
		case BIKE: {
			if (duration <= 0.5) {
				ticket.setPrice(0);
				break;
			} else {
				ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
				break;
			}

		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}

		if (ticketDAO.checkRecurrence(ticket)) {
			ticket.setPrice(0.95 * ticket.getPrice());
		}

	}

}