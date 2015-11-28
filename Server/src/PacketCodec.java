import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;

public class PacketCodec {

	public static String readDelimiter(BufferedReader in) throws IOException{
		char charBuf[] = new char[1];
		String readMsg = "";
		short isdelim = 0;
		int size = 1, totalSize = 0;
		boolean isFirstDelimAppear = false;
		String strSize = "";
		
		// read character before packet delimiter
		while(in.read(charBuf, 0, 1) != -1){//버퍼리더 in의 문자를 하나 읽어 charBuf에 하나 집어넣는다..
			if(!isFirstDelimAppear){
				// read size of packet
				if(Packet.FIELD_DELIM.charAt(0) != charBuf[0]){//'|'와 같지 않으면
					strSize += charBuf[0];//스트링에 넣는다
				}
				else{// '|' 라면
					totalSize = Integer.parseInt(strSize);//지금까지 버퍼리더에서 받아온 String를 int로 바꾸어 토탈사이즈에 넣는다.
					if( totalSize >= 1){//토탈사이즈는 1보다 크거나 같으면
						size = 1;//사이즈는 1이라고 한다.
					}else{//아무것도 없다는 듯이다
						size = totalSize;//사이즈는 0 또는 음수
					}
					charBuf = new char[size];//새로운 char을 지정한다. size가 0이나 음수면? totalSize가 처음부터 '|'을 받을 일이 없기 때문인가?
					isFirstDelimAppear = true;//첫 '|'를 만났으므로 다음 구분자 '?'을 찾는다
				}
			}
			// Packet.PK_DELIM == '?'
			else if(charBuf[0] == '?'){
				readMsg += charBuf[0];//readMsg에 '?'를 추가한다.
				isdelim = 1;
				break;
			} else {
				readMsg += charBuf[0];
				totalSize -= size;//토탈사이즈에 1만큼 빼준다. 토탈사이즈는 처음 받아들인 String을 int형으로 바꾼것을 의미한다. 
				continue;
			}
		}
		
		// remove '\n'
		while(in.read(charBuf, 0, 1) != -1)
		{
			if(charBuf[0] == '\n'){
				break;
			}
		}
		
		// if there isn't delimiter
		if(isdelim == 0 && charBuf[0]  != '\0'){
			System.out.println("MSG DELIM IS NOT FOUND!!");
		}
		return readMsg;
	}
	
	public static Packet decodeHeader(String src) throws IOException{
		String type, data;
		int size;
		Scanner s = new Scanner(src).useDelimiter("\\"+Packet.FIELD_DELIM);
		
		type = s.next();
		s.skip(Packet.FIELD_DELIM);
		
		s.useDelimiter("\\"+Packet.PK_DELIM);
		data = s.next();
		
		return new Packet(type, data);
	}
}
