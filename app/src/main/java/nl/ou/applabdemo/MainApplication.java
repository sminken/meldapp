package nl.ou.applabdemo;

import org.acra.ACRA;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.SubscriberExceptionEvent;

public class MainApplication extends nl.ou.applablib.MainApplication {
	static {
		EventBus.builder().addIndex(new EventBusIndex()).installDefaultEventBus();
	}

	static public EventBus getEventBus() {
		return EventBus.getDefault();
	}

	@Override
	public void crashReportingCreated() {
		getEventBus().register(this);
	}

	@Subscribe
	public void onEvent(SubscriberExceptionEvent event) {
		event.throwable.printStackTrace();
		ACRA.getErrorReporter().handleSilentException(event.throwable);
	}
}