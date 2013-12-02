package com.LTH.aprofile.Classes;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.app.Activity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

//import android.util.Log;

/*
 * This class tries to send a broadcast UDP packet over your wifi network to discover the boxee service. 
 */

import android.content.*;
import android.net.wifi.*;
import java.net.*;
import java.util.*;

import com.LTH.aprofile.MainActivity;
import com.LTH.aprofile.Classes.Sensors.ProximitySensor;
import com.LTH.aprofile.Preferences.VibrationPreference;

public class ProfileExchanger {
	private static final int PORT = 2624;

	MulticastSocket receiveSock;
	DatagramSocket transmitSock;
	private WifiManager.MulticastLock mLock;
	private ProximitySensor proxSens;

	protected StringBuilder logText = new StringBuilder();

	private MainActivity activity;

	// flags in header message
	final static int BROADCAST_REQUEST = 0;
	final static int BROADCAST_ACCEPT = 1;
	final static int PROFILE_SEND = 2;

	private boolean broadcastAllowed;

	// bouncing animation (only active when broadcasting)
	private View animView;

	String myId;

	public ProfileExchanger(MainActivity activity) {
		this.activity = activity;
		proxSens = new ProximitySensor(activity, this);

		broadcastAllowed = false;
		resume();

	}

	public void resume() {
		try {

			transmitSock = new DatagramSocket();

			WifiManager wifi = (WifiManager) activity
					.getSystemService(Context.WIFI_SERVICE);
			myId = wifi.getConnectionInfo().getMacAddress();

			mLock = wifi.createMulticastLock("pseudo-ssdp");
			mLock.acquire();
			receiveSock = makeMulticastListenSocket();
			receiveSock.joinGroup(ssdpSiteLocalV6());

			Thread t = new Thread(new ReceiveMsg(), "multicast listener");
			t.start();

		} catch (IOException e) {
		}
	}

	public void pause() {
		receiveSock.close();
		transmitSock.close();
		mLock.release();
	}

	public void startBroadcasting(View view) {
		broadcastAllowed = true;
		this.animView = view;
		
//		 TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f,
//		            -50.0f, 0.0f);         
//		    animation.setDuration(300); 
//		    animation.setRepeatCount(30); 
//		    animation.setRepeatMode(Animation.REVERSE);
//		    animation.setInterpolator(new DecelerateInterpolator());
//		    
		    RotateAnimation rotation = new RotateAnimation(-8.0f, 8.0f,
	                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
	               0.0f);
		    rotation.setDuration(700); 
		    rotation.setRepeatCount(20); 
		    rotation.setRepeatMode(Animation.REVERSE);
		    rotation.setInterpolator(new DecelerateInterpolator());
		   
		    
		    rotation.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// disallow sharing profile when animation has finished
				broadcastAllowed = false;
			}
			
			
		});
		    view.startAnimation(rotation);
		sendBroadcastRequest();

	}

	private void sendBroadcastRequest() {
		if (broadcastAllowed && proxSens.proximityNear()) {
			Packet packet = new Packet(BROADCAST_REQUEST, myId, "BROADCAST");
			sendMessage("" + packet);

		}
	}

	public void sendMessage(final String msg) {

		Runnable r = new Runnable() {
			public void run() {
				try {
					byte[] bytes = msg.getBytes("UTF-8");
					transmitSock.send(new DatagramPacket(bytes, bytes.length,
							ssdpSiteLocalV6(), PORT));
				} catch (IOException e) {
				}
			}
		};
		new Thread(r, "transmit").start();
	}

	private class ReceiveMsg implements Runnable {
		public void run() {

			byte[] buffer = new byte[4 << 10];
			DatagramPacket pkt = new DatagramPacket(buffer, buffer.length);

			try {
				while (true) {
					receiveSock.receive(pkt);

					String message = new String(pkt.getData(), 0,
							pkt.getLength(), "UTF-8");

					Packet receivedPacket = new Packet(message);
					int flag = receivedPacket.getFlag();
					String sender = receivedPacket.getSender();

					// do nothing if packet was sent by me
					if (receivedPacket.checkSender(myId)) {

					} else if (flag == BROADCAST_REQUEST) {

						Packet packet = new Packet(BROADCAST_ACCEPT, myId,
								sender);
						sendMessage("" + packet);

						// send profile if a broadcast accept was received
					} else if (flag == BROADCAST_ACCEPT
							&& receivedPacket.checkReceiver(myId)) {

						VibrationPreference
								.vibrate(VibrationPreference.VIBRATE_SHARE_PROFILE_SEND);

						String sendProf = MainActivity.currentProfile
								.profileToString();

						Packet packet = new Packet(PROFILE_SEND, myId, sender,
								sendProf);
						sendMessage("" + packet);

						
						//animate when profile was sent
						Animation anim = animView.getAnimation();
						animView.clearAnimation();
						anim = new RotateAnimation(360.0f, 0.0f,
								Animation.RELATIVE_TO_SELF, 0.5f,
								Animation.RELATIVE_TO_SELF, 0.0f);
						anim.setDuration(1000);

						anim.setInterpolator(new AccelerateDecelerateInterpolator());
						animView.setAnimation(anim);
						anim.start();
						broadcastAllowed = false;

						// receive profile if it was sent to me
					} else if (flag == PROFILE_SEND
							&& receivedPacket.checkReceiver(myId)) {
						VibrationPreference
								.vibrate(VibrationPreference.VIBRATE_SHARE_PROFILE_RECEIVE);
						String profile = receivedPacket.getBody();

						Profile receivedProfile = Profile.profileFromString(
								profile, activity);
						MainActivity.settings.addProfile(receivedProfile);
						MainActivity.targetProfile = receivedProfile;
						activity.newProfileConnected();

					}

				}
			} catch (Exception e) {

			}
		}
	}

	private MulticastSocket makeMulticastListenSocket() throws IOException {
		if (true) {
			MulticastSocket rval = new MulticastSocket(PORT);
			NetworkInterface nif = NetworkInterface.getByName("wlan0");
			if (null != nif) {
				rval.setNetworkInterface(nif);
			}
			return rval; // this should have worked
		}

		InetAddress addr = pickWlan0IPV6();

		return new MulticastSocket(new InetSocketAddress(addr, PORT));
	}

	private InetAddress pickWlan0IPV6() throws SocketException {
		NetworkInterface nif = NetworkInterface.getByName("wlan0");

		InetAddress addr = null;
		Enumeration<InetAddress> en = nif.getInetAddresses();
		while (en.hasMoreElements()) {
			InetAddress x = en.nextElement();
			if (x instanceof Inet6Address)
				addr = x;
			else if (addr == null)
				addr = x;
		}
		return addr;
	}

	public static InetAddress ssdpSiteLocalV6() throws UnknownHostException {
		return InetAddress.getByName("FF05::c");
	}

	public void proximityChanged(Boolean proximityNear) {

		if (proximityNear) {
			resume();
			if (broadcastAllowed)
				sendBroadcastRequest();

		} else
			pause();

	}

}

class Packet {
	private int flag;
	private String sender;
	private String receiver;
	private String body;

	public Packet(String fromString) {
		String[] explode = fromString.split("#");
		String header = explode[0];

		String[] separateHeader = header.split(";");
		flag = Integer.parseInt(separateHeader[0]);
		sender = separateHeader[1];
		receiver = separateHeader[2];

		body = explode[1];
	}

	public Packet(int flag, String sender, String receiver) {
		this(flag, sender, receiver, "NO BODY");
	}

	public Packet(int flag, String sender, String receiver, String body) {
		this.flag = flag;
		this.sender = sender;
		this.receiver = receiver;
		this.body = body;
	}

	@Override
	public String toString() {
		String header = flag + ";" + sender + ";" + receiver + "#";
		String packet = header + body;
		return packet;
	}

	public Boolean checkSender(String sender) {
		return ((this.sender).equals(sender));
	}

	public Boolean checkReceiver(String receiver) {
		return ((this.receiver).equals(receiver));
	}

	public int getFlag() {
		return flag;
	}

	public String getSender() {
		return sender;
	}

	public String getBody() {
		return body;

	}

}