
public class AddFriendAck {
	private int answer;
	
	public int getAnswer() {
		return answer;
	}
	public void setAnswerOk() {
		answer = Packet.SUCCESS;
	}
	public void setAnswerFail() {
		answer = Packet.FAIL;
	}

}
