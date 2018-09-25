package app.service.authorization.constants;

import app.service.authorization.util.ExternalProperties;

public class Constants {

	public static final String PROJECT_ID = ExternalProperties.getAppConfig("app.gc.project.id");
	public static final String GB_TXN_ID_KEY = "Global Transaction Id";
}
