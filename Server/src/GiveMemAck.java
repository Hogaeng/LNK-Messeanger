
public class GiveMemAck {

	private int memNum;
	private String memberName;
	private String memberId;
	
	public int getmemNum()
	{
		return memNum;
	}
	public void setmemberNum(int arg)
	{
		memNum = arg;
	}
	
	public String getmemberName()
	{
		return memberName;
	}
	public void setmemberName(String[] arg)
	{
		memberName = PacketCodec.preEncodeAck(memNum, arg);
	}
	
	public String getmemberId()
	{
		return memberId;
	}
	public void setmemberId(String[] arg)
	{
		memberName = PacketCodec.preEncodeAck(memNum, arg);
	}
}
