package nl.ou.applablib;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatDelegate;
import android.text.format.Time;
import android.util.Log;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.config.ACRAConfiguration;
import org.acra.config.ACRAConfigurationException;
import org.acra.config.ConfigurationBuilder;
import org.acra.sender.HttpSender;

import java.util.ArrayList;

@ReportsCrashes(
				formUri = "https://www.bitpowder.com/android/report.lua",
				customReportContent = {ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION, ReportField.AVAILABLE_MEM_SIZE, ReportField.BRAND, ReportField.CRASH_CONFIGURATION, ReportField.CUSTOM_DATA, ReportField.INSTALLATION_ID, ReportField.IS_SILENT, ReportField.REPORT_ID, ReportField.PACKAGE_NAME, ReportField.PHONE_MODEL, ReportField.PRODUCT, ReportField.SHARED_PREFERENCES, ReportField.STACK_TRACE, ReportField.THREAD_DETAILS, ReportField.TOTAL_MEM_SIZE, ReportField.USER_APP_START_DATE, ReportField.USER_CRASH_DATE},
				reportType = HttpSender.Type.JSON,
				mode = ReportingInteractionMode.TOAST,
				resToastText = android.R.string.unknownName)
public class MainApplication extends Application {
	public static final String TAG = "MainApplication";
	public static ArrayList<Throwable> throwables = new ArrayList<>();

	static {
		AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);

		boolean debug = BuildConfig.DEBUG;

		try {
			ConfigurationBuilder builder = new ConfigurationBuilder(this)
							.setResToastText(R.string.crash_toast_text);
			if (debug)
				builder.setFormUri("http://www.bitpowder.com/android/report-debug.lua");
			ACRAConfiguration config = builder.build();
			ACRA.init(this, config);
		} catch (ACRAConfigurationException exception) {
			exception.printStackTrace();
		}

		try {
			PackageInfo manager = getPackageManager().getPackageInfo(getPackageName(), 0);
			final Time curDate = new Time();
			curDate.set(manager.lastUpdateTime);
			ACRA.getErrorReporter().putCustomData("UPDATED_ON", curDate.format3339(false));
		} catch (PackageManager.NameNotFoundException e) {
		} catch (Exception e) {
		}
		try {
			ACRA.getErrorReporter().putCustomData("MAX_HEAP", Long.valueOf(Runtime.getRuntime().maxMemory()/1024/1024).toString());
			ACRA.getErrorReporter().putCustomData("CORES", Integer.valueOf(Runtime.getRuntime().availableProcessors()).toString());
		} catch (Exception e) {
		}

		SafeHandler.setExceptionHandler(new SafeHandler.ExceptionHandler() {
			@Override
			public void handle(Exception e) {
				handleException(e);
			}
		});

		if (debug) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
							.detectAll()
							.penaltyLog()
							.build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
							.detectLeakedSqlLiteObjects()
							.penaltyLog()
							.penaltyDeath()
							.build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
							.penaltyLog()
							.build());
		}

		ArrayList<Throwable> throwables = MainApplication.throwables;
		MainApplication.throwables = null;
		if (throwables != null) // should not happen, but got a crash report on Android 4.1.2 on a GT-S7710 stating it did...
			for (Throwable t : throwables)
				ACRA.getErrorReporter().handleSilentException(t);

		crashReportingCreated();
	}

	public void crashReportingCreated() {
		throw new UnsupportedOperationException("subclasses should override this method");
	}

	public static void handleException(Throwable e) {
		e.printStackTrace();
		if (throwables != null)
			throwables.add(e);
		else
			ACRA.getErrorReporter().handleSilentException(e);
	}

	public static void handleException(Throwable e, String description) {
		ACRA.getErrorReporter().putCustomData("description", description);
		handleException(e);
		ACRA.getErrorReporter().removeCustomData("description");
	}
}
