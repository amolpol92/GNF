package app.constants;

import app.util.ExternalProperties;

public class ConstantsURL {

	// ServiceURLs
	public static final String DLP_SERVICE_URL = ExternalProperties.getURL("app.module.dlp.service.url");

	public static final String DLP_INSPECT_URL = ExternalProperties.getURL("app.module.dlp.service.inspect.url");
	public static final String DLP_DEIDENTIFY_URL = ExternalProperties
			.getURL("app.module.dlp.service.deidentify.url");
	public static final String AUTHORIZE_URL = ExternalProperties.getURL("app.service.authorize.url");
	

}
