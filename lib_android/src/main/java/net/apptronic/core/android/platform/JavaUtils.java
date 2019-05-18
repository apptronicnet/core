package net.apptronic.core.android.platform;

class JavaUtils {

    void suspendCurrentThread(long timeInMillis) {
        synchronized (this) {
            try {
                wait(timeInMillis);
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }

}
