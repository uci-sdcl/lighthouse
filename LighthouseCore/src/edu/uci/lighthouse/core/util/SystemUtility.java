package edu.uci.lighthouse.core.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

import org.apache.log4j.Logger;

import edu.uci.lighthouse.core.ntp.NtpMessage;

public class SystemUtility {
	
	private static final String serverName = "pool.ntp.org";
	private static int lastTimeOffset = 0;
	private static int syncInterval = 5;
	private static long syncCounter = 0;
	
	private static Logger logger = Logger.getLogger(SystemUtility.class);
	
	private static int getLocalClockOffset() throws IOException{
		double localClockOffset = 0;
		DatagramSocket socket = new DatagramSocket();
		try {
			// Send request
			InetAddress address = InetAddress.getByName(serverName);
			byte[] buf = new NtpMessage().toByteArray();
			DatagramPacket packet = new DatagramPacket(buf, buf.length,
					address, 123);

			// Set the transmit timestamp *just* before sending the packet
			// ToDo: Does this actually improve performance or not?
			NtpMessage.encodeTimestamp(packet.getData(), 40, (System
					.currentTimeMillis() / 1000.0) + 2208988800.0);

			socket.send(packet);
			
			// Get response
//			System.out.println("NTP request sent, waiting for response...\n");
			packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
			
			// Immediately record the incoming timestamp
			double destinationTimestamp =
				(System.currentTimeMillis()/1000.0) + 2208988800.0;
			
			
			// Process response
			NtpMessage msg = new NtpMessage(packet.getData());
			
			localClockOffset =
				((msg.receiveTimestamp - msg.originateTimestamp) +
				(msg.transmitTimestamp - destinationTimestamp)) / 2;

		} finally {
			socket.close();
		}
//		System.out.println(localClockOffset*1000);
		return (int)(localClockOffset*1000);
	}

	public synchronized static Date getNTPTime(){
		try {
			if ((syncCounter % syncInterval)==0) {
				int offset = getLocalClockOffset();
				System.out.println(offset+" "+syncCounter+" "+syncInterval);
				if (Math.abs(offset) > Math.abs(lastTimeOffset)){
					syncInterval = syncInterval <= 1 ? 1 : syncInterval / 2;
//					syncCounter = -1;
				} else {
				syncInterval *= 3;
				
				}
				
				lastTimeOffset = offset;
			}
			syncCounter = (syncCounter % Long.MAX_VALUE) + 1;
			
			
		} catch (IOException e) {
			logger.error(e,e);
		}
		Date result = new Date(System.currentTimeMillis()-lastTimeOffset);
		return result;
	}
	
	public static void main(String[] args){
		for (int i=0;i<10000;i++)
//		System.out.println(SystemUtility.getNTPTime());
			SystemUtility.getNTPTime();
		System.out.println("syncCounter:"+syncCounter+" syncInterval:"+syncInterval+" offset:"+lastTimeOffset);
	}
	
}
