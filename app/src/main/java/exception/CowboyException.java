package exception;

public class CowboyException extends Exception {

	private static final long serialVersionUID = -3492785859395455589L;

	public static final int INVALID_RESPONSE_CODE = -1000;

	public static final int NETWORK_ERROR = 0;

	public static final int CONNECT_TIMEOUT = 0;

	//json解析异常
	public static final int XML_PARSER_EXCEPTION = 0;
	//json转换异常
	public static final int XML_TRANSFORMER_EXCEPTION = 0;
	
	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public CowboyException(int code) {
		super();
		this.code = code;
	}

	public CowboyException(int code, String msg) {
		super(msg);
		this.code = code;
	}

	public CowboyException(int code, String msg, Throwable throwable) {
		super(msg, throwable);
		this.code = code;
	}

	public CowboyException(int code, Throwable throwable) {
		super(throwable);
		this.code = code;
	}
}
