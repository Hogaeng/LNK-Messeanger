
public class MakeRoomAck {
	private int answer;
    public int getAnswer() {
        return answer;
    }
    
    public void setAnswerOk() {
        this.answer = Packet.SUCCESS;
    }
    public void setAnswerFail()
    {
    	this.answer = Packet.FAIL;
    }
}
