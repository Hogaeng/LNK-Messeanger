import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;


public class ThreadTcp implements Runnable{


	
	//private Base64Codec bs64 = new Base64Codec();//�̹��� ����
	private Socket clientSocket = null;//Ŭ���̾�Ʈ ���������� ���� ���ϵ� ����
	
	private PrintWriter out = null;
	private BufferedReader in = null;
	
	private boolean isContinous = false;
	private int user_id = 0;
	
	String inputData;
	Packet rcvPacket;

	public ThreadTcp(Socket clientSocket, boolean isContinous) throws IOException{
		this.clientSocket = clientSocket;
		this.isContinous = isContinous;
		
		inputData = "";

		System.out.println("Client Connect");
	}

	public ThreadTcp(ServerSocket serverSocket, boolean isContinous) throws IOException{
		clientSocket = serverSocket.accept();
		this.isContinous = isContinous;
		
		inputData = "";

		System.out.println("Client Connect");
	}

	
	public void run(){
		try{
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));//Ŭ���̾�Ʈ�� ������ ��ǲ ��Ʈ���� ������ �о�鿩 ���ο� ���� ������ �ִ´�.
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			
			while(isContinous){
				// read packet and decode
				
				rcvPacket = PacketCodec.decodeHeader(inputData);//�׷��� ���� ��Ŷ������(String)�� �ѹ� �� �ڵ��� �־� ����� ���ڵ� �Ѵ�. ���°��� Packet.
				isContinous = handler(rcvPacket, out);//���������� handler�� Packet�� PrintWriter�� �־� ������ ������ ���θ� Ȯ���Ѵ�. 
			}//������ true�� �������� false�� ������������ ��� �����Ѵ�.

			in.close();
			out.close();
			clientSocket.close();//TCP�� ������.
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public boolean handler(Packet src, PrintWriter out)throws IOException
	{
		isContinous = false;
		String sendString; 
		switch(src.getType()){
			case Packet.LOG_REQ:
				System.out.println("Log REQ recevied.");
				LoginReq lo_req = PacketCodec.decodeLoginReq(src.getData());
				LoginAck lo_ack = new LoginAck();
				if(lo_req.getId().equals("Android")&&lo_req.getPassword().equals("123456"))
					lo_ack.setAnswer(Packet.SUCCESS);
				else
					lo_ack.setAnswer(Packet.FAIL);
				
				sendString=PacketCodec.encodeLoginAck(lo_ack);
				try{
				out.println(sendString);
				System.out.println("Log Ack dispatched.");
				}
				catch(Exception e){
					e.printStackTrace();
				}
				break;
		}
		
		return isContinous;
	}

}

