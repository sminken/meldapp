package nl.ou.applabdemo.view;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Arrays;
import java.util.List;

import nl.ou.applabdemo.R;
import nl.ou.applabdemo.domain.GebruikerInfo;
import nl.ou.applabdemo.domain.Gebruiker;
import nl.ou.applabdemo.domain.GebruikerBeheer;
import nl.ou.applabdemo.domain.GebruikerStatus;
import nl.ou.applabdemo.domain.Rol;
import nl.ou.applabdemo.util.ExceptionVertaler;
import nl.ou.applabdemo.util.MeldAppException;
import nl.ou.applabdemo.util.MyFirebaseMessagingService;
import nl.ou.applabdemo.viewmodel.GebruikersBeheerViewModel;

import static nl.ou.applabdemo.domain.Rol.MELDER;

@SuppressWarnings("SpellCheckingInspection")
public class MainActivity extends BaseActivity {

	/*
Public abstract class FirebaseAuth extends Object
The entry point of the Firebase Authentication SDK.
 */
	// Firebase Auth instance variables
	@SuppressWarnings("FieldCanBeLocal")
	private FirebaseAuth mFirebaseAuth;
	@SuppressWarnings("WeakerAccess")
	FirebaseUser mFirebaseUser;

	private static final int RC_SIGN_IN = 9001;
	private static final String CHANNEL_ID = "MELDAPP_OPGELOST";

	// logging
	private static final String TAG = "MainActivity";

	// userBeheer
	private GebruikerBeheer gebruikerBeheer = null;

	// tbv ViewModel
	private GebruikersBeheerViewModel gebruikerBeheerViewModel = null;

	// Device token voor notificatie.
	private String deviceToken = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Zet het token voor dit device.
		getDeviceToken();

		// create ViewModels
		Log.v(TAG, "CreateViewModelUsers");
		createViewModelUsers();

	}
	@Override
	public void onResume() {
        /*
        This is the state in which the app interacts with the user.
         */
		super.onResume();
		Log.v(TAG, "onResume");
		ConnectivityManager cm =
				(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
				activeNetwork.isConnectedOrConnecting();

		if (!isConnected)  {
			Toast.makeText(getApplicationContext(),
                    getString(R.string.internet_connectie),
					Toast.LENGTH_LONG).show();
			finish();
		}
		createNotificationChannel();
	}

	public void onDestroy () {
		super.onDestroy();
		Log.v(TAG, "onDestroy");
	}

	/*
    Methods for viewModels
     */
	private void createViewModelUsers() {
		Log.v(TAG, "InitViewModelUsers");

		gebruikerBeheerViewModel = ViewModelProviders.of(this).get(GebruikersBeheerViewModel.class);

		// attach an observer to the viewmodel methodview
		Log.v(TAG, "RegisterObserverHaalUsers");
		//noinspection Convert2Lambda
		gebruikerBeheerViewModel.haalGebruikers().observe(this, new Observer<List<Gebruiker>>() {
			@Override
			public void onChanged(@Nullable List<Gebruiker> gebruikersLijst) {
				Log.v(TAG, "onChangMethodUsers");
				gebruikerBeheer = new GebruikerBeheer(gebruikersLijst);
				verwerkUser();
			}
		});
	}

	/*
   Methoden for user management
    */

	/**
	 * Deze methode verzorgt het aanmeld proces
	 */
	private void verwerkUser() {
		Log.v(TAG, "processUser");

		// Initialiseer Firebase Auth
		mFirebaseAuth = FirebaseAuth.getInstance();
		mFirebaseUser = mFirebaseAuth.getCurrentUser();

		/*
		 1. Controle of gebruiker al is aangemeld aan de Firebase Database
		  */
		if (mFirebaseUser == null) {
			aanmeldenGoogle();
			return;
		}

		// Haal user aan de hand van de uid waarmee is aangemeld op de Firebase database
		String gebruikerUid = mFirebaseUser.getUid();
		GebruikerInfo gebruikerInfo = gebruikerBeheer.haalGebruikerOp(gebruikerUid);


		/*
		 2. Maak gebruiker aan in firebase collection "Gebruikers" indien deze nog niet voorkomt
		  */
		if (gebruikerInfo == null) {
			voegGebruikerToe(gebruikerUid);
			return;
		}

		/*
		 3. Controleer of gebruiker actief is
		  */
		if (gebruikerInfo.getGebruikerStatus() != GebruikerStatus.ACTIEF) {
			verwerkNietActieveGebruiker();
			return;
		}

		/*
		4. Controleer of gebruiker moet worden afgemeld
		 */

		if (getIntent().hasExtra("gebruiker_afmelden")) {
			afmeldenGebruiker();
			return;
		}

		/*
		5. Update deviceToken bij melder, deze kan namelijk gewijzigd zijn
		   doordat nu een ander device wordt gebruikt.
		 */
		Rol gebruikerRol = gebruikerInfo.getRol();
		Boolean gebruikerNotification = gebruikerInfo.getNotification();

		if (gebruikerRol == MELDER && gebruikerNotification) {
			setDeviceTokenMelder(gebruikerInfo.getUid());
		}


        /*
         6. Start vervolg verwerking op aan de hand van de rol
          */
		hoofdVerwerking(gebruikerRol);
	}

	/**
	 * Deze methode is verantwoordelijk voor het opstarten van het aanmelden
	 * van het aanmelden aan de Firebase Database (authentication)
	 */

	private void aanmeldenGoogle() {
		Log.v(TAG, "signUpGoogle");

		/*
		 Kiezen authentication providers
		 Voorlopig alleen gekozen voor het aanmelden met een google account
		  */
		@SuppressWarnings("ArraysAsListWithZeroOrOneArgument") List<AuthUI.IdpConfig> providers = Arrays.asList(
//                new AuthUI.IdpConfig.EmailBuilder().build(),
//                new AuthUI.IdpConfig.PhoneBuilder().build(),
				new AuthUI.IdpConfig.GoogleBuilder().build());
//                new AuthUI.IdpConfig.FacebookBuilder().build(),
//                new AuthUI.IdpConfig.TwitterBuilder().build())

		startActivityForResult(
				AuthUI.getInstance()
						.createSignInIntentBuilder()
						.setAvailableProviders(providers)
						.build(),
				RC_SIGN_IN);
	}

	/*
	Deze call back methode verzorgt de verdere afhandeling
	nadat het aanmeldproces op de Firebase database is afgerond
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v(TAG, "onActivityResult");
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
//			IdpResponse response = IdpResponse.fromResultIntent(data);
			if (resultCode == RESULT_OK) {
				/*
				Herstart deze activity
				 */
				finish();
				startActivity(getIntent());
			} else {
				/*
				 Aanmelden is geannuleerd door de gebruiker
				 Dan activity beëindigen
				  */
				Toast.makeText(this, R.string.SignUpCancelled,  Toast.LENGTH_LONG).show();
				finish();
			}
		}
	}

	/*
	Deze methode verzorgt het toevoegen van de gebruiker aan de gebruikers-tabel
	 */
	private void voegGebruikerToe(String gebruikerUid){
		Log.v(TAG, "addUser");
		/*
		Start GebruikerAanmeldenNieuweGebruikerActivity
		 */
		Intent startAanmeldenIntent = new Intent(this, GebruikerAanmeldenNieuweGebruikerActivity.class);
		startAanmeldenIntent.putExtra("gebruiker_uid", gebruikerUid);
		startActivity(startAanmeldenIntent);

		//	beeindigActivity();
		finish();
	}

	/*
    Deze methode zorgt voor de afhandeling indien een gebruiker aanmeldt
    die geen actieve status heeft
     */
	private void verwerkNietActieveGebruiker () {

		// geef foutmelding

		Log.d(TAG, "ToastNonActiveUser");
		Toast toast = Toast.makeText(this  , R.string.RequestAccess, Toast.LENGTH_LONG);
		toast.show();
		finish();
	}

	/*
        Deze methode zorgt voor de afhandeling indien een gebruiker aanmeldt
        die geen actieve status heeft
         */
	private void afmeldenGebruiker () {
		/*
		Verwijder listener
		Meldt af bij FireBase
		Geef melding
		 */
		gebruikerBeheerViewModel.haalGebruikers().removeObservers(this);
		Toast toast = Toast.makeText(this  , R.string.SignOutCompleted, Toast.LENGTH_LONG);
		AuthUI.getInstance()
				.signOut(this)
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					public void onComplete(@NonNull Task<Void> task) {
						// user is now signed out
						Log.d(TAG, "ToastSignOut");
						toast.show();
						finish();
					}
				});
		finish();
	}

	/*
    Deze methode zorgt voor het verwerken van actieve gebruikers
     */
	private void hoofdVerwerking(Rol gebruikerRol) {
		Log.v(TAG, "mainProcessing");

		switch (gebruikerRol) {
			case MELDER:
				Intent myIntent = new Intent(MainActivity.this,
						OverzichtMeldingenMelderActivity.class);
				startActivity(myIntent);
				finish();
				break;
			case BEHANDELAAR:
				myIntent = new Intent(MainActivity.this,
						OverzichtMeldingenBehandelaarActivity.class);
				startActivity(myIntent);
				finish();
				break;
			case ADMIN:
				// nog niet opgenomen in app
				// geef foutmelding

				Log.d(TAG, "ToastAdminUser");
				Toast toast = Toast.makeText(this  , R.string.AdminNietMogelijk, Toast.LENGTH_LONG);
				toast.show();
				finish();
				break;
			default:
				throw new RuntimeException(getString(R.string.OnbekendeRol));

		}
		finish();
	}

	/**
	 * Deze methode werkt het deviceToken van de gegeven gebruiker bij
	 * @param uidMelder - de Uid van de gegeven gebruiker
	 */
	private void setDeviceTokenMelder(String uidMelder){

		try {
			gebruikerBeheerViewModel.updateDeviceToken(uidMelder, deviceToken);
		}
		catch (MeldAppException e) {
			Toast.makeText(MainActivity.this, ExceptionVertaler.vertaalException(e.getMessage()), Toast.LENGTH_SHORT).show();;
		}

	}

	/**
	 * De methode haalt het deviceToken van gebruikte device op
	 */
	private void getDeviceToken() {
		// Set device token
		//noinspection Convert2Lambda
		FirebaseInstanceId.getInstance().getInstanceId()
				.addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
					@Override
					public void onComplete(@NonNull Task<InstanceIdResult> task) {

						if (!task.isSuccessful()) {
							return;
						}

						// Get new Instance ID token
						//noinspection ConstantConditions
						deviceToken =  task.getResult().getToken();

					}
				});
	}
	/**
	 * Creeert een noticicatiekanaal voor de meldapp voor versies vanaf API 26
	 */
	public void createNotificationChannel() {
		// Create the NotificationChannel, but only on API 26+ because
		// the NotificationChannel class is new and not in the support library
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			CharSequence name = getString(R.string.channel_name);
			String description = getString(R.string.channel_description);
			int importance = NotificationManager.IMPORTANCE_DEFAULT;
			NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
			channel.setLightColor(Color.RED);
			channel.setDescription(description);
			// Register the channel with the system; you can't change the importance
			// or other notification behaviors after this
			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);
		}
	}
}



