package nl.ou.applabdemo.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import nl.ou.applabdemo.BuildConfig;
import nl.ou.applabdemo.R;
import nl.ou.applablib.AutoUpdate;
import nl.ou.applablib.ChangeLogSimple;
import nl.ou.applablib.WizardFragment;

/**
 * Created by bvgastel on 18-01-2018.
 */

public class BaseActivity extends AppCompatActivity {
	private static final String VERSION = "0.1"; // als changelog weergegeven moet worden, moet je deze veranderen

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (BuildConfig.DEBUG && getIntent() != null && Intent.ACTION_MAIN.equals(getIntent().getAction()))
			AutoUpdate.checkNewVersion(this, getString(R.string.auto_update_url));

//		Eerst popup, WizardFragment van commentaar voorzien, Tweede popup gezijzigd van .show() naar .hide()
//	    Mogelijk is dit in een latere versie weer handig

			//if (savedInstanceState == null) {
//			WizardFragment wizard = WizardFragment.newInstanceIfNeeded(this, R.xml.wizard);
//			if (wizard != null)
//				wizard.show(getSupportFragmentManager(), "wizard");
		//}

		ChangeLogSimple cl = new ChangeLogSimple(this, R.raw.changelog, BaseActivity.VERSION);
		//if ((!cl.firstRunEver() && cl.firstRun()))
			cl.getLogDialog(0).hide();

	}
}
