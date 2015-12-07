
public class MssAck {
	private int answer;
	private String arrTime;
	private String list;
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
    
    public String getArrtime() {
        return arrTime;
    }
    
    public void setArrtime(String arg) {
        this.arrTime = arg;
    }
    public String getlist() {
        return list;
    }
    
    public void setlist(String arg) {
        this.list = arg;
    }
}
