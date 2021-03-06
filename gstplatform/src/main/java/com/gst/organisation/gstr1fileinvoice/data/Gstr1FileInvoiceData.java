package com.gst.organisation.gstr1fileinvoice.data;

import java.util.Collection;

public class Gstr1FileInvoiceData {
	private Long id;
	private String gstin;
	private String fp;
	private String grossTurnover;
	private String fileNo;
	private Integer version;
	private Integer status;
	private String assignedTo;
	private String errorCode;
	private String errorDescriptor;
	private String reviewComments;
	
	
	private Collection<Gstr1FileB2BInvoiceData> gstr1FileB2BInvoiceData;
	
	public Gstr1FileInvoiceData() {
	}


	public Gstr1FileInvoiceData(final Long id, final String gstin, final String fp, final String grossTurnover, final String fileNo, final int version,
			final int status, final String assignedTo, final String errorCode, final String errorDescriptor, final String reviewComments) {

		this.id = id;
		this.gstin = gstin;
		this.fp = fp;
		this.grossTurnover = grossTurnover;
		this.fileNo = fileNo;
		this.version = version;
		this.status = status;
		this.assignedTo = assignedTo;
		this.errorCode = errorCode;
		this.errorDescriptor = errorDescriptor;
		this.reviewComments = reviewComments;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getGstin() {
		return gstin;
	}


	public void setGstin(String gstin) {
		this.gstin = gstin;
	}


	public String getFp() {
		return fp;
	}


	public void setFp(String fp) {
		this.fp = fp;
	}


	public String getGrossTurnover() {
		return grossTurnover;
	}


	public void setGrossTurnover(String grossTurnover) {
		this.grossTurnover = grossTurnover;
	}


	public String getFileNo() {
		return fileNo;
	}


	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}


	public int getVersion() {
		return version;
	}


	public void setVersion(int version) {
		this.version = version;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public String getAssignedTo() {
		return assignedTo;
	}


	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}


	public String getErrorCode() {
		return errorCode;
	}


	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}


	public String getErrorDescriptor() {
		return errorDescriptor;
	}


	public void setErrorDescriptor(String errorDescriptor) {
		this.errorDescriptor = errorDescriptor;
	}


	public String getReviewComments() {
		return reviewComments;
	}


	public void setReviewComments(String reviewComments) {
		this.reviewComments = reviewComments;
	}


	public Collection<Gstr1FileB2BInvoiceData> getGstr1FileB2BInvoiceData() {
		return gstr1FileB2BInvoiceData;
	}


	public void setGstr1FileB2BInvoiceData(Collection<Gstr1FileB2BInvoiceData> gstr1FileB2BInvoiceData) {
		this.gstr1FileB2BInvoiceData = gstr1FileB2BInvoiceData;
	}
	
}
