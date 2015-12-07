
public class RoomAck {
	private int mauNum ;
	private MssAndArrtimeAndUser mau;
	private int memberNum;
	private String member;
	
	public int getMauNum() {
		return mauNum;
	}
	public void setMauNum(int num) {
		mauNum = num;
	}
	
	public MssAndArrtimeAndUser getMau() {
		return mau;
	}
	public void setRoomName(MssAndArrtimeAndUser mau) {
		this.mau = mau;
	}
	
	public int getMemberNum() {
		return memberNum;
	}
	public void setMemberNum(int num) {
		memberNum = num;
	}
	
	public String getMember() {
		return member;
	}
	public void setMember(String member) {
		this.member = member;
	}
}
