package org.contextlogger.android;

interface IRemoteLogger {
	void updateSensorsToRecord();
	boolean isRunning();
	void stopService();
}