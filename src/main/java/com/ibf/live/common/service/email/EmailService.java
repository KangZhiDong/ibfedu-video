package com.ibf.live.common.service.email;

public abstract interface EmailService {
	public abstract void sendMail(String paramString1, String paramString2, String paramString3) throws Exception;

	public abstract void sendBatchMail(String[] paramArrayOfString, String paramString1, String paramString2);

	public abstract void sendMailWithFile(String paramString1, String paramString2, String paramString3,
                                          String[] paramArrayOfString) throws Exception;

	public abstract void sendBatchMailWithFile(String[] paramArrayOfString1, String paramString1, String paramString2,
                                               String[] paramArrayOfString2) throws Exception;
}
