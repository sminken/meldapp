package nl.ou.applablib;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class SafeHandler extends Handler {
    public SafeHandler(Looper looper) {
        super(looper);
    }
    public void dispatchMessage(Message msg) {
        try {
            super.dispatchMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            if (handler != null)
                handler.handle(e);
        }
    }
    static ExceptionHandler handler;
    public static void setExceptionHandler(ExceptionHandler handler) {
        SafeHandler.handler = handler;
    }

    public interface ExceptionHandler {
        public void handle(Exception e);
    }
}
