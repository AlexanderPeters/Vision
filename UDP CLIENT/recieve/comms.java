package recieve;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class comms {
	static int x, y, dist;
	public static int loop = 0;

	public static void main(String[] args) throws IOException {
		@SuppressWarnings("resource")
		DatagramSocket serverSocket = new DatagramSocket(12346);
		byte[] receiveData = new byte[1024];
		// byte[] sendData = new byte[1024];
		System.out.println("this");
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			String sentence = new String(receivePacket.getData());
			String corrected = sentence.replaceAll("\u0000.*", "");
			// System.out.println("RECEIVED: " + corrected);

			reader(corrected);
			loop = 0;
			print();
		}

	}

	public static void reader(String str) {

		List<String> objects = new ArrayList<>();
		//System.out.println(str);
		String[] values = str.split(",");

		while (!values[loop].contains("Last")) {
			//System.out.println(loop);
			objects.add(values[loop]);
			loop++;
		}
		x = Integer.parseInt(objects.get(0));
		y = Integer.parseInt(objects.get(1));
		dist = Integer.parseInt(objects.get(2));
		objects = new ArrayList<>();

	}

	public static void print(){
		System.out.println("X is " + x);
		System.out.println("Y is " + y);
		System.out.println("Distance is " + dist);
		
	}

}
