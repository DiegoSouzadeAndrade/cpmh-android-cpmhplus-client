package br.com.cpmh.plus.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.Objects;

import br.com.cpmh.plus.R;

public class PasswordRecoveryActivity extends AppCompatActivity implements View.OnClickListener
{
	private static final String TAG = "PasswordRecovery";
	private FirebaseAuth firebaseAuthentication;

	private TextInputEditText emailField;
	private Button passwordRecoveryButton;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_recovery);

		firebaseAuthentication = FirebaseAuth.getInstance();

		emailField = findViewById(R.id.email_field);

		Bundle bundle = getIntent().getExtras();

		if (bundle != null && bundle.get("email") != null)
		{
			emailField.setText(bundle.getString("email"));
		}

		passwordRecoveryButton = findViewById(R.id.password_recovery_button);
		passwordRecoveryButton.setOnClickListener(this);
	}

	private void attemptPasswordRecovery()
	{
		final String email = Objects.requireNonNull(emailField.getText()).toString();

		if (!isEmailValid(email))
		{
			emailField.requestFocus();
		}
		else
		{
			firebaseAuthentication.fetchSignInMethodsForEmail(email).addOnCompleteListener(new FetchSignInMethodsForEmailListener());
		}
	}

	private boolean isEmailValid(String email)
	{
		if (email == null || email.isEmpty())
		{
			emailField.setError(getString(R.string.error_empty_field));
			return false;
		}
		else return Patterns.EMAIL_ADDRESS.matcher(email).matches();

	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.password_recovery_button:
				attemptPasswordRecovery();
				passwordRecoveryButton.setEnabled(false);
				break;
		}
	}

	private class FetchSignInMethodsForEmailListener implements OnCompleteListener<SignInMethodQueryResult>
	{

		@Override
		public void onComplete(@NonNull Task<SignInMethodQueryResult> task)
		{
			if (task.isSuccessful())
			{
				Log.i(TAG, "fetchSignInMethodsForEmail: Success!");

				if (Objects.requireNonNull(task.getResult().getSignInMethods()).isEmpty())
				{
					emailField.setError(getString(R.string.error_wrong_email));
				}
				else
				{
					final String email = Objects.requireNonNull(emailField.getText()).toString();
					firebaseAuthentication.sendPasswordResetEmail(email).addOnCompleteListener(new PasswordRecoveryAttemptListener());
				}
			}
			else
			{
				Log.i(TAG, "fetchSignInMethodsForEmail: Failed!");

				try
				{
					throw Objects.requireNonNull(task.getException());
				}
				catch (FirebaseAuthInvalidCredentialsException invalidCredentialException)
				{
					emailField.setError(getString(R.string.error_badly_formatted_email));
					emailField.requestFocus();
				}
				catch (Exception exception)
				{
					//Todo: add Firebase Crash Reporting.
					Log.i(TAG, exception.getMessage());
					Toast.makeText(getBaseContext(), R.string.error_password_recovery_failed, Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	private class PasswordRecoveryAttemptListener implements OnCompleteListener<Void>
	{

		@Override
		public void onComplete(@NonNull Task<Void> task)
		{
			if (task.isSuccessful())
			{
				Log.i(TAG, "passwordRecoveryAttempt: Success!");

				Toast.makeText(getBaseContext(), "", Toast.LENGTH_LONG).show();
			}
			else
			{
				Log.i(TAG, "passwordRecoveryAttempt: Failed!");

				try
				{
					throw Objects.requireNonNull(task.getException());
				}
				catch (FirebaseAuthInvalidUserException invalidUserException)
				{
					emailField.setError(getString(R.string.error_wrong_email));
					emailField.requestFocus();
				}
				catch (Exception exception)
				{
					//Todo: add Firebase Crash Reporting.
					Log.i(TAG, exception.getMessage());
					Toast.makeText(getBaseContext(), R.string.error_password_recovery_failed, Toast.LENGTH_SHORT).show();
				}
				finally
				{
					passwordRecoveryButton.setEnabled(true);
				}
			}
		}
	}
}
