package com.LTH.aprofile.Classes;

/*
 * Copyright 2013, Qualcomm Innovation Center, Inc.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import java.util.Timer;
import java.util.TimerTask;

import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.BusException;
import org.alljoyn.bus.BusObject;
import org.alljoyn.bus.SignalEmitter;
import org.alljoyn.bus.Status;
import org.alljoyn.bus.annotation.BusSignalHandler;
import org.json.JSONObject;

import com.LTH.aprofile.MainActivity;
import com.LTH.aprofile.Classes.Sensors.ProximitySensor;
import com.LTH.aprofile.Preferences.BrightnessPreference;
import com.LTH.aprofile.Preferences.Preference;
import com.LTH.aprofile.Preferences.SoundLevelPreference;
import com.LTH.aprofile.Preferences.VibrationPreference;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;

import android.os.Message;
import android.util.Log;

public class ProfileExchanger {

	// flags in header message
	final static int BROADCAST_REQUEST = 0;
	final static int BROADCAST_ACCEPT = 1;
	final static int PROFILE_SEND = 2;

	protected final int myId;

	ProximitySensor proxSens;
	private Activity activity;



	/* Load the native alljoyn_java library. */
	static {

		System.loadLibrary("alljoyn_java");
	}

	public ProfileExchanger(final Activity activity) {
		HandlerThread busThread = new HandlerThread("BusHandler");
		busThread.start();
		mBusHandler = new Handler(busThread.getLooper(),
				new BusHandlerCallback());

		mBusHandler.sendEmptyMessage(BusHandlerCallback.CONNECT);

		myId = (int) (Math.random() * 10000);
		proxSens = new ProximitySensor(activity);
		this.activity = activity;



	}

	public void sendBroadcastRequest() {
		String header = BROADCAST_REQUEST + ";" + myId + ";" + "-1";
		Message msg = mBusHandler.obtainMessage(BusHandlerCallback.CHAT,
				new PingInfo(header, "BROADCAST_REQUEST"));

		mBusHandler.sendMessage(msg);
	}

	private static final String TAG = "SessionlessChat";

	/*
	 * Handler for UI messages This handler updates the UI depending on the
	 * message received.
	 */
	private static final int MESSAGE_CHAT = 1;
	private static final int MESSAGE_POST_TOAST = 2;

	private class PingInfo {
		private String senderName;
		private String message;

		public PingInfo(String senderName, String message) {
			this.senderName = senderName;
			this.message = message;
		}

		public String getSenderName() {
			return this.senderName;
		}

		public String getMessage() {
			return this.message;
		}
	}

	private Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_CHAT:
				/* Add the chat message received to the List View */
				String ping = (String) msg.obj;
				break;
			case MESSAGE_POST_TOAST:
				/* Post a toast to the UI */

				break;
			default:
				break;
			}

			return true;
		}
	});

	/* Handler used to make calls to AllJoyn methods. See onCreate(). */
	private Handler mBusHandler;

	// *****************************ALL THE IMPORTANT STUFF / HERE IS WHERE THE
	// SENDING AND RECEIVING TAKES PLACE

	/* The class that is our AllJoyn service. It implements the ChatInterface. */
	class ChatService implements ChatInterface, BusObject {
		public ChatService(BusAttachment bus) {
			this.bus = bus;
		}

		/*
		 * This is the Signal Handler code which has the interface name and the
		 * name of the signal which is sent by the client. It prints out the
		 * string it receives as parameter in the signal on the UI.
		 * 
		 * This code also prints the string it received from the user and the
		 * string it is returning to the user to the screen.
		 */
		@BusSignalHandler(iface = "org.alljoyn.bus.samples.slchat", signal = "Chat")
		public void Chat(String senderName, String message) {
			Log.i(TAG, "Signal  : " + senderName + ": " + message);
			sendUiMessage(MESSAGE_CHAT, senderName + ": " + message);

			// added by me
			String[] separateHeader = senderName.split(";");
			int flag = Integer.parseInt(separateHeader[0]);
			int sender = Integer.parseInt(separateHeader[1]);
			int receiver = Integer.parseInt(separateHeader[2]);

			// do nothing if message was sent by me or if proximity sensor is
			// not near
			Log.d("prox near", "2");
			Log.d("prox near", proxSens.proximityNear() + "");
			if (sender == myId || !proxSens.proximityNear()) {
				Log.d("prox near", proxSens.proximityNear() + "");
			} else if (flag == BROADCAST_REQUEST) {
				String header = BROADCAST_ACCEPT + ";" + myId + ";" + sender;
				Message msg = mBusHandler.obtainMessage(
						BusHandlerCallback.CHAT, new PingInfo(header,
								"BROADCAST_ACCEPT"));
				mBusHandler.sendMessage(msg);

				// send profile if a broadcast accept was received
			} else if (flag == BROADCAST_ACCEPT && receiver == myId) {


				VibrationPreference
						.vibrate(VibrationPreference.VIBRATE_SHARE_PROFILE_SEND);

				String sendProf = MainActivity.currentProfile.profileToString();

				String header = PROFILE_SEND + ";" + myId + ";" + sender;
				Message msg = mBusHandler
						.obtainMessage(BusHandlerCallback.CHAT, new PingInfo(
								header, sendProf));
				mBusHandler.sendMessage(msg);

				// receive profile if it was sent to me
			} else if (flag == PROFILE_SEND && receiver == myId) {
				VibrationPreference
						.vibrate(VibrationPreference.VIBRATE_SHARE_PROFILE_RECEIVE);
				Profile receivedProfile = Profile.profileFromString(message,
						activity);
				MainActivity.settings.addProfile(receivedProfile);
				MainActivity.currentProfile = receivedProfile;

			}

		}

		// *******************************************************************CRAP

		/* Helper function to send a message to the UI thread. */
		private void sendUiMessage(int what, Object obj) {
			mHandler.sendMessage(mHandler.obtainMessage(what, obj));
		}

		private BusAttachment bus;
	}

	/* This Callback class will handle all AllJoyn calls. See onCreate(). */
	class BusHandlerCallback implements Handler.Callback {

		/* The AllJoyn BusAttachment */
		private BusAttachment mBus;

		/* The AllJoyn SignalEmitter used to emit sessionless signals */
		private SignalEmitter emitter;

		private ChatInterface mChatInterface = null;
		private ChatService myChatService = null;

		/* These are the messages sent to the BusHandlerCallback from the UI. */
		public static final int CONNECT = 1;
		public static final int DISCONNECT = 2;
		public static final int CHAT = 3;

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			/* Connect to the bus and register to obtain chat messages. */
			case CONNECT: {

				mBus = new BusAttachment("aProfile",
						BusAttachment.RemoteMessage.Receive);

				/*
				 * Create and register a bus object
				 */
				myChatService = new ChatService(mBus);
				Status status = mBus.registerBusObject(myChatService,
						"/ChatService");
				if (Status.OK != status) {
					logStatus("BusAttachment.registerBusObject()", status);
					return false;
				}

				/*
				 * Connect the BusAttachment to the bus.
				 */
				status = mBus.connect();
				logStatus("BusAttachment.connect()", status);
				if (status != Status.OK) {
					// finish();
					return false;
				}

				/*
				 * We register our signal handler which is implemented inside
				 * the ChatService
				 */
				status = mBus.registerSignalHandlers(myChatService);
				if (status != Status.OK) {
					Log.i(TAG, "Problem while registering signal handler");
					return false;
				}

				/*
				 * Add rule to receive chat messages(sessionless signals)
				 */
				status = mBus.addMatch("sessionless='t'");
				if (status == Status.OK) {
					Log.i(TAG, "AddMatch was called successfully");
				}

				break;
			}

			/* Release all resources acquired in connect. */
			case DISCONNECT: {
				/*
				 * It is important to unregister the BusObject before
				 * disconnecting from the bus. Failing to do so could result in
				 * a resource leak.
				 */
				mBus.disconnect();
				mBusHandler.getLooper().quit();
				break;
			}
			/*
			 * Call the service's Ping method through the ProxyBusObject.
			 * 
			 * This will also print the String that was sent to the service and
			 * the String that was received from the service to the user
			 * interface.
			 */
			case CHAT: {
				try {
					if (emitter == null) {
						/*
						 * Create an emitter to emit a sessionless signal with
						 * the desired message. The session ID is set to zero
						 * and the sessionless flag is set to true.
						 */
						emitter = new SignalEmitter(myChatService, 0,
								SignalEmitter.GlobalBroadcast.Off);
						emitter.setSessionlessFlag(true);
						/* Get the ChatInterface for the emitter */
						mChatInterface = emitter
								.getInterface(ChatInterface.class);
					}
					if (mChatInterface != null) {
						PingInfo info = (PingInfo) msg.obj;
						/*
						 * Send a sessionless signal using the chat interface we
						 * obtained.
						 */
						Log.i(TAG, "Sending chat " + info.getSenderName()
								+ ": " + info.getMessage());
						mChatInterface.Chat(info.getSenderName(),
								info.getMessage());
					}
				} catch (BusException ex) {
					logException("ChatInterface.Chat()", ex);
				}
				break;
			}
			default:
				break;
			}
			return true;
		}
	}

	private void logStatus(String msg, Status status) {
		String log = String.format("%s: %s", msg, status);
		if (status == Status.OK) {
			Log.i(TAG, log);
		} else {
			Message toastMsg = mHandler.obtainMessage(MESSAGE_POST_TOAST, log);
			mHandler.sendMessage(toastMsg);
			Log.e(TAG, log);
		}
	}

	private void logException(String msg, BusException ex) {
		String log = String.format("%s: %s", msg, ex);
		Message toastMsg = mHandler.obtainMessage(MESSAGE_POST_TOAST, log);
		mHandler.sendMessage(toastMsg);
		Log.e(TAG, log, ex);
	}


}
