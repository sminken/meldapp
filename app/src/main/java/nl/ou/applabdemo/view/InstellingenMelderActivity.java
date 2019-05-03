package nl.ou.applabdemo.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.List;

import nl.ou.applabdemo.R;
import nl.ou.applabdemo.domain.Gebruiker;
import nl.ou.applabdemo.domain.GebruikerBeheer;
import nl.ou.applabdemo.domain.GebruikerInfo;
import nl.ou.applabdemo.util.ExceptionVertaler;
import nl.ou.applabdemo.util.MeldAppException;
import nl.ou.applabdemo.viewmodel.GebruikersBeheerViewModel;

@SuppressWarnings("SpellCheckingInspection")
public class InstellingenMelderActivity extends AppCompatActivity {

    // Viewmodel voor het beheer van gebruikers.
    private GebruikersBeheerViewModel gebruikerBeheerViewModel = null;
    private GebruikerBeheer gebruikerBeheer = null;

    // Device token voor notificatie.
    private String deviceToken = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instellingen_melder);

        // Zet het token voor dit device.
        getDeviceToken();

        // Creëer de viewmodels voor gebruikers.
        createViewModelGebruikerBeheer();
    }

    /**
     * Creeert de viewmodel voor gebruikers.
     */
    private void createViewModelGebruikerBeheer() {

        // Viewmodel voor het beheer van gebruikers.
        gebruikerBeheerViewModel = ViewModelProviders.of(this).get(GebruikersBeheerViewModel.class);

        // Voeg de observer aan de viewmodel toe.
        //noinspection Convert2Lambda
        gebruikerBeheerViewModel.haalGebruikers().observe(this, new Observer<List<Gebruiker>>() {
            @Override
            public void onChanged(@Nullable List<Gebruiker> gebruikers) {

                gebruikerBeheer = new GebruikerBeheer(gebruikers);

                // Gegevens aangemelde gebruiker.
                GebruikerInfo gebruikerInfo = gebruikerBeheer.haalAangemeldeGebruiker();

                // Initialiseer de switches in instellingen van deze gebruiker.
                initSwitches(gebruikerInfo);
            }
        });
    }

    /**
     * Initialiseert de switches van de view.
     */
    private void initSwitches(GebruikerInfo gebruikerInfo) {

        // Switch voor het aan en uitzetten van de notificaties.
        final Switch switchNotificatie = findViewById(R.id.switch_notificatie);

        //noinspection Convert2Lambda
        switchNotificatie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean notificationSwitch) {

                // Uid van deze aangemelde gebruiker.
                String uidMelder = gebruikerInfo.getUid();
                boolean notificationMelder = gebruikerInfo.getNotification();

                // Wijzig het device token alleen als de status van de notification wijzigt. Dus
                // notificatie melder is false en de notificatie switch is true of visa versa.
                if (notificationMelder != notificationSwitch ) {

                    // Wijzig op basis de notificatue switch de instellingen bij de melder.
                    try {
                        if (notificationSwitch) {
                            // Zet het ontvangen van notificaties aan.
                            gebruikerBeheerViewModel.wijzigNotificatieGebruiker(uidMelder, true);
                            // Wijzig het device token.
                            gebruikerBeheerViewModel.updateDeviceToken(uidMelder,deviceToken);
                        } else {
                            // Zet het ontvangen van notificaties uit. Het device token blijft gehandhaafd.
                            gebruikerBeheerViewModel.wijzigNotificatieGebruiker(uidMelder, false);
                            // Wijzig het device token.
                            gebruikerBeheerViewModel.updateDeviceToken(uidMelder,null);
                        }
                    } catch (MeldAppException e) {
                        Toast.makeText(InstellingenMelderActivity.this, ExceptionVertaler.vertaalException(e.getMessage()), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        switchNotificatie.setChecked(gebruikerInfo.getNotification());
    }

    /**
     * De methode haalt het deviceToken van gebruikte device op.
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

}
