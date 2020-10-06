package com.dimaslanjaka.tools.Firebase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.dimaslanjaka.tools.Helpers.firebase.SharedFirebasePreferences;
import com.dimaslanjaka.tools.Helpers.firebase.SharedFirebasePreferencesContextWrapper;
import com.dimaslanjaka.tools.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity
				extends AppCompatActivity
				implements FirebaseAuth.AuthStateListener,
				SharedPreferences.OnSharedPreferenceChangeListener {

	private final String TAG = this.getClass().getName();
	private SharedFirebasePreferences mPreferences;
	//login class
	private DatabaseReference mDatabase;
	private FirebaseAuth mAuth;
	private EditText edtEmail;
	private EditText edtPass;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FirebaseApp.initializeApp(this);
		FirebaseAuth.getInstance().addAuthStateListener(this);
		//FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    /*
    FirebaseAuth.getInstance().signInAnonymously()
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                  Log.w(TAG, "signInAnonymously", task.getException());
                  Toast.makeText(TestActivity.this, "Authentication failed.",
                          Toast.LENGTH_SHORT).show();
                }
              }
            });
     */
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mPreferences != null) {
			mPreferences.keepSynced(true);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mPreferences != null) {
			mPreferences.keepSynced(false);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mPreferences != null) {
			mPreferences.unregisterOnSharedPreferenceChangeListener(this);
		}
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(new SharedFirebasePreferencesContextWrapper(newBase));
	}


	@Override
	public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
		if (firebaseAuth.getCurrentUser() != null) {
			setContentView(R.layout.activity_test);
			mPreferences = SharedFirebasePreferences.getDefaultInstance(this);
			mPreferences.keepSynced(true);
			mPreferences.registerOnSharedPreferenceChangeListener(this);
			mPreferences.pull().addOnPullCompleteListener(new SharedFirebasePreferences.OnPullCompleteListener() {
				@Override
				public void onPullSucceeded(SharedFirebasePreferences preferences) {
					showView();
				}

				@Override
				public void onPullFailed(Exception e) {
					showView();
					Toast.makeText(MainActivity.this, "Fetch failed", Toast.LENGTH_SHORT).show();
				}
			});
		} else {
			//login class
			setContentView(R.layout.firebase_login);
			//variabel tadi untuk memanggil fungsi
			mDatabase = FirebaseDatabase.getInstance().getReference();
			mAuth = FirebaseAuth.getInstance();

			// diatur sesuai id komponennya
			edtEmail = (EditText) findViewById(R.id.tv_email);
			edtPass = (EditText) findViewById(R.id.tv_pass);
			Button btnMasuk = (Button) findViewById(R.id.btn_masuk);
			Button btnDaftar = (Button) findViewById(R.id.btn_daftar);

			//nambahin method onClick, biar tombolnya bisa diklik
			btnMasuk.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					int i = view.getId();
					if (i == R.id.btn_masuk) {
						signIn();
					} else if (i == R.id.btn_daftar) {
						signUp();
					}
				}
			});
			btnDaftar.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					int i = view.getId();
					if (i == R.id.btn_masuk) {
						signIn();
					} else if (i == R.id.btn_daftar) {
						signUp();
					}
				}
			});
			Log.e(TAG, "user not signed in");
		}
	}

	//fungsi signin untuk mengkonfirmasi data pengguna yang sudah mendaftar sebelumnya
	private void signIn() {
		Log.d(TAG, "signIn");
		if (!validateForm()) {
			return;
		}

		//showProgressDialog();
		String email = edtEmail.getText().toString();
		String password = edtPass.getText().toString();

		mAuth.signInWithEmailAndPassword(email, password)
						.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
							@Override
							public void onComplete(@NonNull Task<AuthResult> task) {
								Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
								//hideProgressDialog();

								if (task.isSuccessful()) {
									onAuthSuccess(Objects.requireNonNull(task.getResult().getUser()));
								} else {
									Toast.makeText(getApplicationContext(), "Sign In Failed",
													Toast.LENGTH_SHORT).show();
								}
							}
						});
	}

	//fungsi ini untuk mendaftarkan data pengguna ke Firebase
	private void signUp() {
		Log.d(TAG, "signUp");
		if (!validateForm()) {
			return;
		}

		//showProgressDialog();
		String email = edtEmail.getText().toString();
		String password = edtPass.getText().toString();

		mAuth.createUserWithEmailAndPassword(email, password)
						.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
							@Override
							public void onComplete(@NonNull Task<AuthResult> task) {
								Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
								//hideProgressDialog();

								if (task.isSuccessful()) {
									onAuthSuccess(Objects.requireNonNull(task.getResult().getUser()));
								} else {
									Toast.makeText(getApplicationContext(), "Sign Up Failed",
													Toast.LENGTH_SHORT).show();
								}
							}
						});
	}

	//fungsi dipanggil ketika proses Authentikasi berhasil
	private void onAuthSuccess(FirebaseUser user) {
		String username = usernameFromEmail(Objects.requireNonNull(user.getEmail()));

		// membuat User admin baru
		writeNewAdmin(user.getUid(), username, user.getEmail());

		// restart
		finish();
		startActivity(new Intent(this, this.getClass()));
	}

	/*
			ini fungsi buat bikin username dari email
					contoh email: abcdefg@mail.com
					maka username nya: abcdefg
	 */
	private String usernameFromEmail(String email) {
		if (email.contains("@")) {
			return email.split("@")[0];
		} else {
			return email;
		}
	}

	//fungsi untuk memvalidasi EditText email dan password agar tak kosong dan sesuai format
	private boolean validateForm() {
		boolean result = true;
		if (TextUtils.isEmpty(edtEmail.getText().toString())) {
			edtEmail.setError("Required");
			result = false;
		} else {
			edtEmail.setError(null);
		}

		if (TextUtils.isEmpty(edtPass.getText().toString())) {
			edtPass.setError("Required");
			result = false;
		} else {
			edtPass.setError(null);
		}

		return result;
	}

	// menulis ke Database
	private void writeNewAdmin(String userId, String name, String email) {
		Admin admin = new Admin(name, email);

		mDatabase.child("admins").child(userId).setValue(admin);
	}

	private void showView() {
		getFragmentManager().beginTransaction().replace(R.id.view, new PreferenceFragment()).commitAllowingStateLoss();
		findViewById(R.id.progessBar).setVisibility(View.GONE);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
		showView();
	}

	public static class PreferenceFragment extends android.preference.PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.preferences);
		}
	}
}