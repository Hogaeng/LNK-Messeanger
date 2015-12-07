
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;


public class PacketCodec {
	
	public static String readBuffReader(BufferedReader in) throws IOException{
		char charBuf[] = new char[1];
	
		String src = "";
		
		while(in.read(charBuf, 0, 1) != -1)
		{
			if(charBuf[0] == '\n')
				break;
			src += charBuf[0];
			
		}
		//System.out.println("readBufferReader step two...while state END" + charBuf[0]);
		if(src.equals(""))
			return null;
		return src;
	}
	

	public static Packet decodeHeader(String src) throws IOException{
		String type, data;
		System.out.println("Decode : START...");
		Scanner s = new Scanner(src).useDelimiter("\\"+Packet.FIELD_DELIM);
		
		type = s.next();
		s.skip(Packet.FIELD_DELIM);
		s.useDelimiter("\\"+Packet.PK_DELIM);
		data = s.next();
		System.out.println("Decode : END...");
		return new Packet(type, data);
	}
	
	// About join request
	// Dncode join request packet data
	public static String encodeJoinReq(JoinReq pk_data){
		String data = Packet.JOIN_REQ 
				+ Packet.FIELD_DELIM + pk_data.getName() 
				+ Packet.FIELD_DELIM + pk_data.getId()
				+ Packet.FIELD_DELIM + pk_data.getPassword()
				+ Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
				
		return data;
	}
	public static JoinReq decodeJoinReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		JoinReq dst = new JoinReq();

		dst.setName(s.next());
		s.skip(Packet.FIELD_DELIM);
		dst.setId(s.next());
		s.skip(Packet.FIELD_DELIM);
		dst.setPassword(s.next());

		return dst;
	}
	
	public static String encodeJoinAck(JoinAck pk_data){
		String data = Packet.LOG_ACK + Packet.FIELD_DELIM
				+ Integer.toString(pk_data.getAnswer()) + Packet.FIELD_DELIM;
		
		/*if (pk_data.getAnswer() == Packet.SUCCESS){
		}*/
		
		data += Packet.PK_DELIM;	
		
		return data;
	}

	public static JoinAck decodeJoinAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		JoinAck dst = new JoinAck();

		if(Packet.SUCCESS==s.nextInt())
			dst.setAnswerOk();
		else
			dst.setAnswerFail();
		return dst;
	}
	
	public static String encodeLoginReq(LoginReq pk_data){
		String data = Packet.LOG_REQ 
				+ Packet.FIELD_DELIM + pk_data.getId()
				+ Packet.FIELD_DELIM + pk_data.getPassword()
				+ Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		
		return data;
	}

	public static LoginReq decodeLoginReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		LoginReq dst = new LoginReq();

		dst.setId(s.next());
		s.skip(Packet.FIELD_DELIM);
		dst.setPassword(s.next());

		return dst;
	}
	
	public static String encodeLoginAck(LoginAck pk_data){
		String data = Packet.LOG_ACK
				+ Packet.FIELD_DELIM + Integer.toString(pk_data.getAnswer())
				+ Packet.FIELD_DELIM;
		
		/*if (pk_data.getAnswer() == Packet.SUCCESS){
		}*/
		
		data += Packet.PK_DELIM;	
		
		return data;
	}

	public static LoginAck decodeLoginAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		LoginAck dst = new LoginAck();

		if(s.nextInt()==Packet.SUCCESS)
			dst.setAnswerOk();
		else
			dst.setAnswerFail();
		return dst;
	}
	
	public static String encodeMssReq(MssReq pk_data){
		String data = Packet.MSS_REQ 
				+ Packet.FIELD_DELIM + pk_data.getMessage()
				+ Packet.FIELD_DELIM
			    + Packet.PK_DELIM;
		
		return data;
	}
	
	public static MssReq decodeMssReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		MssReq dst = new MssReq();

		dst.setMessage(s.next());

		return dst;
	}
	
	public static String encodeMssAck(MssAck pk_data){
		String data = Packet.MSS_ACK 
				+ Packet.FIELD_DELIM + pk_data.getAnswer()
				+ Packet.FIELD_DELIM
			    + Packet.PK_DELIM;

		return data;
	}
	public static MssAck decodeMssAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		MssAck dst = new MssAck();
		
		if(Packet.SUCCESS==s.nextInt())
			dst.setAnswerOk();
		else
			dst.setAnswerFail();

		return dst;
	}
	
	public static String encodeInviRoomReq(InviRoomReq pk_data){
		String data = Packet.INVIROOM_REQ 
				+ Packet.FIELD_DELIM
			    + Packet.PK_DELIM;

		return data;
	}
	public static InviRoomReq decodeInviRoomReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		InviRoomReq dst = new InviRoomReq();
		
		return dst;
	}
	
	public static String encodeInviRoomAck(InviRoomAck pk_data ){
		String data = Packet.INVIROOM_ACK 
				+ Packet.FIELD_DELIM + pk_data.getAnswer()
				+ Packet.FIELD_DELIM
			    + Packet.PK_DELIM;

		return data;
	}
	public static InviRoomAck decodeInviRoomAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		InviRoomAck dst = new InviRoomAck();
		
		if(Packet.SUCCESS==s.nextInt())
			dst.setAnswerOk();
		else
			dst.setAnswerFail();

		return dst;
	}
}
