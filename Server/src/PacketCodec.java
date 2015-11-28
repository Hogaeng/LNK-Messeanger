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
		while(in.read(charBuf, 0, 1) != -1){//���۸��� in�� ���ڸ� �ϳ� �о� charBuf�� �ϳ� ����ִ´�..
			if(!isFirstDelimAppear){
				// read size of packet
				if(Packet.FIELD_DELIM.charAt(0) != charBuf[0]){//'|'�� ���� ������
					strSize += charBuf[0];//��Ʈ���� �ִ´�
				}
				else{// '|' ���
					totalSize = Integer.parseInt(strSize);//���ݱ��� ���۸������� �޾ƿ� String�� int�� �ٲپ� ��Ż����� �ִ´�.
					if( totalSize >= 1){//��Ż������� 1���� ũ�ų� ������
						size = 1;//������� 1�̶�� �Ѵ�.
					}else{//�ƹ��͵� ���ٴ� ���̴�
						size = totalSize;//������� 0 �Ǵ� ����
					}
					charBuf = new char[size];//���ο� char�� �����Ѵ�. size�� 0�̳� ������? totalSize�� ó������ '|'�� ���� ���� ���� �����ΰ�?
					isFirstDelimAppear = true;//ù '|'�� �������Ƿ� ���� ������ '?'�� ã�´�
				}
			}
			// Packet.PK_DELIM == '?'
			else if(charBuf[0] == '?'){
				readMsg += charBuf[0];//readMsg�� '?'�� �߰��Ѵ�.
				isdelim = 1;
				break;
			} else {
				readMsg += charBuf[0];
				totalSize -= size;//��Ż����� 1��ŭ ���ش�. ��Ż������� ó�� �޾Ƶ��� String�� int������ �ٲ۰��� �ǹ��Ѵ�. 
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
