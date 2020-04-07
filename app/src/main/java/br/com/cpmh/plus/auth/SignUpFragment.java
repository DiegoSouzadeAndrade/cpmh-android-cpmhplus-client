package br.com.cpmh.plus.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.cpmh.plus.R;

/**
 *
 */
public class SignUpFragment extends Fragment
{
	private static final String TAG = "SignUpFragment";
	/**
	 *
	 */
	public SignUpFragment() { }

	/**
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_sign_up, container, false);
	}
}
