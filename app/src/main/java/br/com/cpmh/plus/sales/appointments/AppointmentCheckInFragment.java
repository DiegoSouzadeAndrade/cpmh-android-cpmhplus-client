package br.com.cpmh.plus.sales.appointments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.cpmh.plus.R;

public class AppointmentCheckInFragment extends Fragment
{
	private static final String TAG = "AppointmentCheckInFragment";

	public AppointmentCheckInFragment() { }

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_appointment_check_in, container, false);
	}

}
