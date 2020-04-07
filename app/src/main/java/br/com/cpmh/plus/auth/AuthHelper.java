package br.com.cpmh.plus.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

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
public class AuthHelper implements OnFailureListener
{
	private final Context context;
	private final FirebaseAuth auth;
	private final SharedPreferences preferences;

	/**
	 * @param context
	 */
	public AuthHelper(Context context)
	{
		this.context = context;
		preferences = Objects.requireNonNull(context.getSharedPreferences(context.getString(R.string.auth_shared_preferences), Context.MODE_PRIVATE));

		auth = FirebaseAuth.getInstance();
	}

	/**
	 *
	 */
	public void validateAuth()
	{
		String storedEmail = preferences.getString(context.getString(R.string.stored_email), "");
		String storedPassword = preferences.getString(context.getString(R.string.stored_password), "");

		try
		{
			MessageDigest digest = MessageDigest.getInstance(context.getString(R.string.algorithm_message_digest));
			byte[] password = context.getString(R.string.secret_key_password).getBytes(context.getString(R.string.secret_key_password_charset));

			digest.update(password, 0, password.length);
			byte[] secretKeyDigest = digest.digest();

			SecretKeySpec secretKey = new SecretKeySpec(secretKeyDigest, context.getString(R.string.algorithm_cipher));

			Cipher cipher = Cipher.getInstance(context.getString(R.string.algorithm_cipher));
			cipher.init(Cipher.DECRYPT_MODE, secretKey);

			byte[] decodedPassword;
			decodedPassword = Base64.decode(storedPassword, Base64.DEFAULT);
			decodedPassword = cipher.doFinal(decodedPassword);

			String decryptedPassword = new String(decodedPassword);

			byte[] decodedEmail;
			decodedEmail = Base64.decode(storedEmail, Base64.DEFAULT);
			decodedEmail = cipher.doFinal(decodedEmail);

			String decryptedEmail = new String(decodedEmail);

			AuthCredential credential = EmailAuthProvider.getCredential(decryptedEmail, decryptedPassword);

			Objects.requireNonNull(auth.getCurrentUser()).reauthenticate(credential).addOnFailureListener(this);

		}
		catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e)
		{
			//TODO: .
			e.printStackTrace();
		}
	}

	/**
	 * @param exception
	 */
	@Override
	public void onFailure(@NonNull Exception exception)
	{
		Activity activity = (Activity) context;

		Intent intent = new Intent(activity, LoginActivity.class);
		activity.startActivity(intent);
		activity.finish();
	}
}
