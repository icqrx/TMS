package jp.co.isb.tms;


public enum ECarryOutStatus {
	YES("1"),
	NO("0");

	private String text;

	ECarryOutStatus(String text) {
		this.text = text;
	}

	public String getValue() {
		return this.text;
	}

}