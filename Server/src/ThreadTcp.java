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

import javax.swing.plaf.synth.SynthSpinnerUI;


public class ThreadTcp implements Runnable{
	
	//private Base64Codec bs64 = new Base64Codec();//
	private Socket clientSocket = null;//
	
	private PrintWriter out = null;
	private BufferedReader in = null;
	
	private boolean isContinous = false;
	private int user = 0;
	private String user_id;
	private int presentRoom=0;
	private String RoomName;
	private Database db;
	String inputData;
	Packet rcvPacket;
	ResultSet rs;
	public ThreadTcp(Socket clientSocket, boolean isContinous) throws IOException{
		this.clientSocket = clientSocket;
		this.isContinous = isContinous;

		System.out.println("Client Connect");
		db = new Database();
		if(db.connect())
			System.out.println("DB Connect");
		else
			System.out.println("DB Fail...");
	}

	public ThreadTcp(ServerSocket serverSocket, boolean isContinous) throws IOException{
		clientSocket = serverSocket.accept();
		this.isContinous = isContinous;

		System.out.println("Client Connect");
		if(db.connect())
			System.out.println("DB Connect");
		else
			System.out.println("DB Fail...");
	}

	
	public void run(){
		try{
			System.out.println("ThreadTcp run step zero");
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));//
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			System.out.println("ThreadTcp run step one");
			while(isContinous){
				inputData = PacketCodec.readBuffReader(in);
				if(inputData==null){
					continue;
				}
				rcvPacket = PacketCodec.decodeHeader(inputData);//
				System.out.println("ThreadTcp step four");
				isContinous = handler(rcvPacket, out);//
				rcvPacket=null;
			}//
			in.close();
			out.close();
			clientSocket.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public boolean handler(Packet src, PrintWriter out)throws IOException
	{
		String sendString;
		
		switch(src.getType()){
			case Packet.LOG_REQ:
				System.out.println("Log REQ recevied.");
				LoginReq lo_req = PacketCodec.decodeLoginReq(src.getData());
				LoginAck lo_ack = new LoginAck();
				db.query = "select Id, Pw, Dbid from "+Database.memberData;//+" where * Dbid";
				rs = db.excuteStatementReturnRs();
				try{
					while(rs.next())
					{
						if(lo_req.getId().equals(rs.getString("Id"))){
							System.out.println("Login Ack : Id_Success");
							if(lo_req.getPassword().equals(rs.getString("Pw"))){
								lo_ack.setAnswerOk();
								user=rs.getInt("Dbid");
								user_id = rs.getString("Id");
								System.out.println("Login Ack : Success");	
								break;
							}
						}	
					}
					if(lo_ack.getAnswer()!=Packet.SUCCESS){
						lo_ack.setAnswerFail();
						System.out.println("Login Ack : Fail");
					}
					sendString = PacketCodec.encodeLoginAck(lo_ack);
					try{
						out.println(sendString);
						System.out.println("Login Ack dispatched.");
						}
						catch(Exception e){
							e.printStackTrace();
						}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				break;
				
			case Packet.JOIN_REQ:
				System.out.println("Join REQ recevied");
				JoinReq jo_req = PacketCodec.decodeJoinReq(src.getData());
				JoinAck jo_ack = new JoinAck();
				db.query = "select Id from "+Database.memberData;//+" where * Dbid";
				rs = db.excuteStatementReturnRs();
				try{
					while(rs.next())
					{
						if(jo_req.getId().equals(rs.getString("Id"))){
							jo_ack.setAnswerFail();
							System.out.println("Join Ack : Fail");
							break;
						}	
					}
					
					if(jo_ack.getAnswer()!=Packet.FAIL){
						jo_ack.setAnswerOk();
						db.query = "insert into "+Database.memberData+" (Name,Id,Pw) values "
								+"('"+jo_req.getName()+"','"+jo_req.getId()+"','"+jo_req.getPassword()+"')";
						db.excuteStatement();
						System.out.println("Join Ack : Success");
					}
					sendString = PacketCodec.encodeJoinAck(jo_ack);
					try{
						out.println(sendString);
						System.out.println("JOin Ack dispatched.");
						}
						catch(Exception e){
							e.printStackTrace();
						}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				break;
			case Packet.MSS_REQ:
				System.out.println("MSS REQ recevied");
				MssReq mss_req = PacketCodec.decodeMssReq(src.getData());
				MssAck mss_ack = new MssAck();
				mss_req.getMessage();
				db.query = "insert into "+Database.messBoard+" (RoomId, Id, ) values "
						+"('"+make_req.getRoomName()+"')";
				db.excuteStatement();
				
				mss_ack.setAnswerOk();
				sendString = PacketCodec.encodeMssAck(mss_ack);
				out.println(sendString);
				System.out.println("Mss Ack dispatched.");
				
				break;
				
			case Packet.GIVEMEM_REQ:
				System.out.println("GiveMem REQ recevied");
				GiveMemReq give_req = PacketCodec.decodeGiveMemReq(src.getData());
				GiveMemAck give_ack = new GiveMemAck();
				db.query = "select Id, Name from "+Database.memberData;//+" where * Dbid";
				rs = db.excuteStatementReturnRs();
				int count = 0;
				String name="";
				String Id="";
				try{
					while(rs.next())
					{
						count++;
						Id+=rs.getString("Id");
						Id+=Packet.SMALLDELIM;
						name+=rs.getString("Name");
						name+=Packet.SMALLDELIM;
					}
					
					give_ack.setmemberNum(count);
					give_ack.setmemberName(name);
					give_ack.setmemberId(Id);
					sendString = PacketCodec.encodeGiveMemAck(give_ack);
					try{
						out.println(sendString);
						System.out.println("GiveMem Ack dispatched.");
						}
						catch(Exception e){
							e.printStackTrace();
						}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				break;
			case Packet.MAKEROOM_REQ:
				System.out.println("MAKEROOM REQ recevied");
				MakeRoomReq make_req = PacketCodec.decodeMakeRoomReq(src.getData());
				MakeRoomAck make_ack = new MakeRoomAck();
			
				try{
				db.query = "insert into "+Database.roomList+" (RoomName) values "
						+"('"+make_req.getRoomName()+"')";
				db.excuteStatement();
				RoomName = make_req.getRoomName();
				System.out.println("MakeRoom...");
				
				/*db.query = "select RoomId from "+Database.roomList+" where RoomName = "+RoomName;
				rs = db.excuteStatementReturnRs();
				rs.next();
				presentRoom = rs.getInt("RoomId");*/
				
				
				/*db.query = "insert into "+Database.messBoard+" (RoomId, Id) values "
						+"('"+presentRoom+"','"+user_id+"')";
				db.excuteStatement();
				System.out.println("and put user in the Room!");*/
				
				}
				catch(Exception e)
				{
					e.printStackTrace();
					make_ack.setAnswerFail();
					System.out.println("MAKEROOM Ack Fail...");
					sendString = PacketCodec.encodeMakeRoomAck(make_ack);
					try{
						out.println(sendString);
						System.out.println("MAKEROOM Ack dispatched");
						}
						catch(Exception t)
						{
							t.printStackTrace();
						}
					break;
				}
				make_ack.setAnswerOk();
				System.out.println("MAKEROOM Ack Success...");
				sendString = PacketCodec.encodeMakeRoomAck(make_ack);
				try{
					out.println(sendString);
					System.out.println("MAKEROOM Ack dispatched");
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				
				break;
		}
		
		return isContinous;
	}
}

