package jp.co.isb.tms;

public enum EStatus_CD {
	Undelivered("000"),
	Absence("010"), 
	Finish("020"), 
	Finish_NoResult("021"),
	On_Delivery("030"),
	Under_Investigation("040"),
	Re_Delivery("050"),
	Cancellation("060"),
	ReceiptRefusal("070"),
	Transfer("080"),
	Others("200"),
	All("300");

	private String text;

	EStatus_CD(String text) {
		this.text = text;
	}

	public String getValue() {
		return this.text;
	}

}