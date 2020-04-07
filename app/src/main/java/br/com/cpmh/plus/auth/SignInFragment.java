package br.com.cpmh.plus.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import br.com.cpmh.plus.R;

/**
 *
 */
public class SignInFragment extends Fragment implements View.OnClickListener, TextInputEditText.OnEditorActionListener
{
	private static final String TAG = "SignInFragment";

	private FirebaseAuth auth;
	private FirebaseRemoteConfig remoteConfig;

	private SharedPreferences preferences;

	private TextInputEditText emailField;
	private TextInputEditText passwordField;

	private long attemptsTimer;
	private long attemptsCount;

	private Button signInButton;
	private Button passwordRecoveryButton;

	/**
	 *
	 */
	public SignInFragment() {}

	/**
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_sign_in, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		auth = FirebaseAuth.getInstance();

		remoteConfig = FirebaseRemoteConfig.getInstance();
		remoteConfig.setDefaults(R.xml.remote_config_defaults);

		emailField = view.findViewById(R.id.email_field);
		emailField.setOnEditorActionListener(this);

		passwordField = view.findViewById(R.id.password_field);
		passwordField.setOnEditorActionListener(this);

		signInButton = view.findViewById(R.id.sign_in_button);
		signInButton.setOnClickListener(this);

		passwordRecoveryButton = view.findViewById(R.id.password_recovery_button);
		passwordRecoveryButton.setOnClickListener(this);

		preferences = Objects.requireNonNull(getContext()).getSharedPreferences(getString(R.string.auth_shared_preferences), Context.MODE_PRIVATE);

		attemptsCount = preferences.getLong(getString(R.string.sign_in_failed_attempts_count), 0);
		attemptsTimer = preferences.getLong(getString(R.string.sign_in_attempts_disabled_timer), 0);

		attemptsTimer -= (System.currentTimeMillis() - preferences.getLong(getString(R.string.sign_in_attempts_disabled_date), 0));

		if (attemptsTimer <= 0)
		{
			enableAttempts();
		}

		if (attemptsCount >= remoteConfig.getLong(getString(R.string.sign_in_failed_attempts_limit)))
		{
			disableAttempts();
		}
	}

	/**
	 *
	 */
	private void disableAttempts()
	{
		signInButton.setEnabled(false);

		int remainingTime = (int) (attemptsTimer / 60000);

		if (remainingTime < 1)
		{
			remainingTime = 1;
		}

		Toast.makeText(getActivity(), String.format(getResources().getQuantityString(R.plurals.error_too_many_sign_in_attempts, remainingTime, remainingTime), remoteConfig.getLong(getString(R.string.sign_in_attempts_disabled_duration))), Toast.LENGTH_LONG).show();

		Log.i(TAG, "");

		if (attemptsTimer == 0)
		{
			attemptsTimer = (remoteConfig.getLong(getString(R.string.sign_in_attempts_disabled_duration)) * 60000);
		}

		Timer timer = new Timer(attemptsTimer);
		timer.start();
	}

	/**
	 *
	 */
	private void enableAttempts()
	{
		Log.i(TAG, "");

		attemptsCount = 0;
		attemptsTimer = 0;

		SharedPreferences.Editor editor;
		editor = preferences.edit();
		editor.putLong(getString(R.string.sign_in_failed_attempts_count), attemptsCount);
		editor.putLong(getString(R.string.sign_in_attempts_disabled_timer), attemptsTimer);
		editor.apply();

		signInButton.setEnabled(true);
	}

	/**
	 *
	 */
	private void attemptSignIn()
	{
		emailField.setError(null);
		passwordField.setError(null);

		final String email = Objects.requireNonNull(emailField.getText()).toString();
		final String password = Objects.requireNonNull(passwordField.getText()).toString();

		boolean abort = false;
		View focus = null;

		if (isPasswordInvalid(password))
		{
			focus = passwordField;
			abort = true;
		}

		if (isEmailInvalid(email))
		{
			focus = emailField;
			abort = true;
		}

		if (abort)
		{
			focus.requestFocus();
			signInButton.setEnabled(true);
			passwordRecoveryButton.setEnabled(true);
		}
		else
		{
			auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new SignInAttemptListener());
		}
	}

	/**
	 * @param email
	 * @return
	 */
	private boolean isEmailInvalid(String email)
	{
		if (email == null || email.isEmpty())
		{
			emailField.setError(getString(R.string.error_empty_field));
			return true;
		}
		else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
		{
			emailField.setError(getString(R.string.error_badly_formatted_email));
			return true;
		}

		return false;
	}

	/**
	 * @param password
	 * @return
	 */
	private boolean isPasswordInvalid(String password)
	{
		if (password == null || password.isEmpty())
		{
			passwordField.setError(getString(R.string.error_empty_field));
			return true;
		}
		else if (password.length() < remoteConfig.getLong(getString(R.string.password_minimum_length)))
		{
			passwordField.setError(getString(R.string.error_weak_password));
			return true;
		}

		return false;
	}

	/**
	 * @param view
	 */
	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.sign_in_button:

				signInButton.setEnabled(false);
				passwordRecoveryButton.setEnabled(false);

				if (attemptsCount >= remoteConfig.getLong(getString(R.string.sign_in_failed_attempts_limit)))
				{
					disableAttempts();
				}
				else
				{
					attemptSignIn();
				}

				break;

			case R.id.password_recovery_button:

				Intent intent = new Intent(getActivity(), PasswordRecoveryActivity.class);

				final String email = Objects.requireNonNull(emailField.getText()).toString();

				if (Patterns.EMAIL_ADDRESS.matcher(email).matches())
				{
					intent.putExtra(getString(R.string.stored_password), email);
				}

				startActivity(intent);

				break;
		}
	}

	/**
	 * @param view
	 * @param actionId
	 * @param event
	 * @return
	 */
	@Override
	public boolean onEditorAction(TextView view, int actionId, KeyEvent event)
	{
		emailField.setError(null);
		passwordField.setError(null);

		switch (view.getId())
		{
			case R.id.password_field:

				if (actionId == EditorInfo.IME_ACTION_DONE)
				{
					if (isPasswordInvalid(Objects.requireNonNull(passwordField.getText()).toString()))
					{
						passwordField.requestFocus();
					}
					else
					{
						if (attemptsCount < remoteConfig.getLong(getString(R.string.sign_in_failed_attempts_limit)))
						{
							signInButton.performClick();
						}
					}
				}
				return true;

			case R.id.email_field:

				if (actionId == EditorInfo.IME_ACTION_NEXT)
				{
					if (isEmailInvalid(Objects.requireNonNull(emailField.getText()).toString()))
					{
						emailField.requestFocus();
					}
					else
					{
						passwordField.requestFocus();
					}
				}
				return true;

			default:

				return false;
		}
	}

	/**
	 *
	 */
	private class SignInAttemptListener implements OnCompleteListener<AuthResult>
	{
		@Override
		public void onComplete(@NonNull Task<AuthResult> task)
		{
			if (task.isSuccessful())
			{
				Log.i(TAG, "signInAttempt: Success!");

				SharedPreferences.Editor editor;

				editor = preferences.edit();

				try
				{
					//Todo: add the reauthentication procedure.
					MessageDigest digest = MessageDigest.getInstance(getString(R.string.algorithm_message_digest));
					byte[] password = getString(R.string.secret_key_password).getBytes(getString(R.string.secret_key_password_charset));

					digest.update(password, 0, password.length);
					byte[] secretKeyDigest = digest.digest();

					SecretKeySpec secretKey = new SecretKeySpec(secretKeyDigest, getString(R.string.algorithm_cipher));

					Cipher cipher = Cipher.getInstance(getString(R.string.algorithm_cipher));
					cipher.init(Cipher.ENCRYPT_MODE, secretKey);

					byte[] encryptedEmail = cipher.doFinal(Objects.requireNonNull(emailField.getText()).toString().getBytes());
					byte[] encryptedPassword = cipher.doFinal(Objects.requireNonNull(passwordField.getText()).toString().getBytes());

					editor.putString(getString(R.string.stored_email), Base64.encodeToString(encryptedEmail, Base64.DEFAULT));
					editor.putString(getString(R.string.stored_password), Base64.encodeToString(encryptedPassword, Base64.DEFAULT));
				}
				catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e)
				{
					//TODO: .
					e.printStackTrace();
				}

				editor.apply();

				Intent intent = new Intent(getActivity(), WelcomeActivity.class);
				startActivity(intent);

				Objects.requireNonNull(getActivity()).finish();
			}
			else
			{
				Log.i(TAG, "signInAttempt: Failed!");

				attemptsCount++;

				try
				{
					throw Objects.requireNonNull(task.getException());
				}
				catch (FirebaseAuthInvalidUserException invalidUserException)
				{
					emailField.setError(getString(R.string.error_wrong_email));
					emailField.requestFocus();
				}
				catch (FirebaseAuthInvalidCredentialsException invalidCredentialException)
				{
					passwordField.setError(getString(R.string.error_wrong_password));
					passwordField.requestFocus();
				}
				catch (Exception exception)
				{
					//Todo: add Firebase Crash Reporting.

					Log.i(TAG, exception.getMessage());
					Toast.makeText(getActivity(), R.string.error_authentication_failed, Toast.LENGTH_SHORT).show();

					attemptsCount--;
				}
				finally
				{
					SharedPreferences.Editor editor;
					editor = preferences.edit();
					editor.putLong(getString(R.string.sign_in_failed_attempts_count), attemptsCount);
					editor.putLong(getString(R.string.sign_in_attempts_disabled_date), System.currentTimeMillis());
					editor.apply();

					if (attemptsCount >= remoteConfig.getLong(getString(R.string.sign_in_failed_attempts_limit)))
					{
						disableAttempts();
					}
					else
					{
						signInButton.setEnabled(true);
						passwordRecoveryButton.setEnabled(true);
					}
				}
			}
		}
	}

	/**
	 *
	 */
	private class Timer extends CountDownTimer
	{
		/**
		 * @param millisInFuture
		 */
		Timer(long millisInFuture)
		{
			super(millisInFuture, 1000);
		}

		/**
		 * @param millisUntilFinished
		 */
		@Override
		public void onTick(long millisUntilFinished)
		{
			attemptsTimer = millisUntilFinished;

			Log.i(TAG, "Remaining time: " + attemptsTimer);

			SharedPreferences.Editor editor;

			editor = preferences.edit();
			editor.putLong(getString(R.string.sign_in_attempts_disabled_timer), attemptsTimer);
			editor.apply();
		}

		/**
		 *
		 */
		@Override
		public void onFinish()
		{
			enableAttempts();
		}
	}
}
