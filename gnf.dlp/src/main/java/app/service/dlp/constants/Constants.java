package app.service.dlp.constants;

import app.service.dlp.utils.ExternalProperties;

public class Constants {

	public static final String PROJECT_ID = ExternalProperties.getAppConfig("app.gc.project.id");
	// ServiceOptions.getDefaultProjectId()
	
	public static final String GB_TXN_ID_KEY = "Global Transaction Id";

}
