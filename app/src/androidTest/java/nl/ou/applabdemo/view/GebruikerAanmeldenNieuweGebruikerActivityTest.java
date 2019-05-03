package nl.ou.applabdemo.view;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import nl.ou.applabdemo.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@SuppressWarnings("SpellCheckingInspection")
public class GebruikerAanmeldenNieuweGebruikerActivityTest {

    @Rule
    // derde parameter: zorgt ervoor dat activity niet automatisch wordt gestart
    public ActivityTestRule<GebruikerAanmeldenNieuweGebruikerActivity>
            mGebruikerAanmeldenNieuweGebruikerActivityTestRule =
            new ActivityTestRule<>(GebruikerAanmeldenNieuweGebruikerActivity.class, true, false);


    @Test
    public void openView() throws Exception {
        vulIntentEnStartActivity();

        // Controleer of juiste scherm wordt getoond
        onView(withId(R.id.info_aanmelden_nieuwe_gebruiker)).
                check(matches(withText("Aanmelden nieuwe gebruiker")));

        // Controleer velden op scherm
        onView(withId(R.id.textView_voornaam)).check(matches(withText("Voornaam")));
        onView(withId(R.id.editText_voornaam)).check(matches(withText("")));
        onView(withId(R.id.textView_achternaam)).check(matches(withText("Achternaam")));
        onView(withId(R.id.editText_achternaam)).check(matches(withText("")));
        onView(withId(R.id.textView_telefoonnummer)).check(matches(withText("Telefoonnummer")));
        onView(withId(R.id.editText_telefoonnummer)).check(matches(withText("")));
        onView(withId(R.id.buttonAanmeldenMelder)).check(matches(withText("Als Melder")));
        onView(withId(R.id.buttonAanmeldenBehandelaar)).check(matches(withText("Als Behandelaar")));
        onView(withId(R.id.buttonAnnuleren)).check(matches(withText("Annuleren")));
        onView(withId(R.id.textView_foutmelding)).check(matches(withText("")));

    }



    @Test
    public void achternaamVerplicht() {

        vulIntentEnStartActivity();

         /* Controleer foutmelding igv niet ingevulde achternaam
             - hetzelfde scherm moet worden getoond
             - foutmelding op scherm
         */
        onView(withId(R.id.buttonAanmeldenMelder)).perform(click());
        onView(withId(R.id.info_aanmelden_nieuwe_gebruiker)).
                check(matches(withText("Aanmelden nieuwe gebruiker")));
        onView(withId(R.id.textView_foutmelding)).
                check(matches(withText("Achternaam moet ingevuld zijn.")));
    }


    /*
    Hulp methoden
     */
    private void vulIntentEnStartActivity() {
        // Intent vullen met verplichte waarde UID
        Intent intent = new Intent();
        intent.putExtra("gebruiker_uid", "adb128888");

        // starten activity
        mGebruikerAanmeldenNieuweGebruikerActivityTestRule.launchActivity(intent);
    }
}

