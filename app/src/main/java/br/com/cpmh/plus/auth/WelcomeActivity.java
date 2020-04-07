package br.com.cpmh.plus.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.cpmh.plus.R;
import br.com.cpmh.plus.dashboard.DashboardActivity;

/**
 *
 */
public class WelcomeActivity extends AppCompatActivity implements Runnable
{
	private static final String TAG = "WelcomeActivity";
	private static final int TIMEOUT = 1000;

	private Handler handler;

	private FirebaseAuth auth;
	private FirebaseUser user;

	/**
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		handler = new Handler();
		auth = FirebaseAuth.getInstance();
	}

	/**
	 *
	 */
	@Override
	public void onStart()
	{
		super.onStart();

		user = auth.getCurrentUser();

		handler.postDelayed(this, TIMEOUT);
	}

	/**
	 *
	 */
	@Override
	public void run()
	{
		Log.i(TAG, "User authenticated.");

		Intent intent = new Intent(this, DashboardActivity.class);
		startActivity(intent);
		finish();
	}
}
