package br.com.cpmh.plus.sales.appointments;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;

import br.com.cpmh.plus.R;

public class CheckInFragment extends Fragment implements View.OnClickListener, LocationListener

{
	private LocationManager locationManager;
	private Location location;
	private Button checkInButton;
	private CheckIn checkIn;

	public CheckInFragment() { }

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_check_in, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		//checkInButton = view.findViewById(R.id.check_in_button);

		locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
		if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
		{
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600000, 50, this);
		}
	}

	@Override
	public void onClick(View view)
	{
	/*	switch (view.getId())
		{
			case R.id.check_in_button:
				checkInButton.setEnabled(false);
				attemptToCheckIn();
				break;
		}
	*/
	}

	private void attemptToCheckIn()
	{
		/*
		if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED) || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_GRANTED))
		{
			if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION))
			{

			}
			else
			{

				location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}
		}
		else
		{
			String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};
			ActivityCompat.requestPermissions(getActivity(), permissions, 200);
		}
		*/
	}

	@Override
	public void onLocationChanged(Location location)
	{
		this.location = location;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{

	}

	@Override
	public void onProviderEnabled(String provider)
	{

	}

	@Override
	public void onProviderDisabled(String provider)
	{

	}
}
