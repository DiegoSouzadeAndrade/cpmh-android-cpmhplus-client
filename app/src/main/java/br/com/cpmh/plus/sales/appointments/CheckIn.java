package br.com.cpmh.plus.sales.appointments;

import java.io.Serializable;
import java.util.Date;

import br.com.cpmh.plus.sales.appointments.Appointment;

public class CheckIn implements Serializable
{
	String latitude;
	String longitude;
	String altitude;
	String locationName;
	Appointment appointment;
	Date date;

	public CheckIn() { }

	public String getLatitude()
	{
		return latitude;
	}

	public void setLatitude(String latitude)
	{
		this.latitude = latitude;
	}

	public String getLongitude()
	{
		return longitude;
	}

	public void setLongitude(String longitude)
	{
		this.longitude = longitude;
	}

	public String getAltitude()
	{
		return altitude;
	}

	public void setAltitude(String altitude)
	{
		this.altitude = altitude;
	}

	public String getLocationName()
	{
		return locationName;
	}

	public void setLocationName(String locationName)
	{
		this.locationName = locationName;
	}

	public Appointment getAppointment()
	{
		return appointment;
	}

	public void setAppointment(Appointment appointment)
	{
		this.appointment = appointment;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}
}
