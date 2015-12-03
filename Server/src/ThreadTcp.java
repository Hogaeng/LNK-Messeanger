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


	
	//private Base64Codec bs64 = new Base64Codec();//이미지 맵핑
	private Socket clientSocket = null;//클라이언트 소켓이지만 서버 소켓도 들어간다
	
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
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));//클라이언트가 보내준 인풋 스트림을 리더로 읽어들여 새로운 버퍼 생성해 넣는다.
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			
			while(isContinous){
				// read packet and decode
				
				rcvPacket = PacketCodec.decodeHeader(inputData);//그렇게 나온 패킷데이터(String)를 한번 더 코덱에 넣어 헤더를 디코드 한다. 나온것은 Packet.
				isContinous = handler(rcvPacket, out);//최종적으로 handler에 Packet과 PrintWriter를 넣어 오류가 났는지 여부를 확인한다. 
			}//오류로 true를 내뱉으면 false를 내뱉을때까지 계속 실행한다.

			in.close();
			out.close();
			clientSocket.close();//TCP를 끝낸다.
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

