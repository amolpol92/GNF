package app.constants;

import app.util.ExternalProperties;

/**
 * @author AdarshSinghal
 *
 */
public class Constants {

	public static final String PROJECT_ID = ExternalProperties.getAppConfig("app.gc.project.id");
	// ServiceOptions.getDefaultProjectId()

	public static final String SUBSCRIPTION_ID = ExternalProperties.getAppConfig("app.gc.pubsub.subscription.s2");

	public static final String APPLICATION_DATE_FORMAT =ExternalProperties.getAppConfig("app.default.dateformat");
	public static final String APPLICATION_TIMEZONE = ExternalProperties.getAppConfig("app.default.timezone");
	
	public static final String TRUE = "true";
	public static final String IN_PROGRESS = "In-progress";
	public static final String DELIVERED = "Delivered";
	
	public static final String GB_TXN_ID_KEY = "Global Transaction Id";
	
	public static final String RETRY_COUNTER = "GNF Retry Message Counter";
	public static final String RETRY_FLAG = "GNF Retry Message flag";
	public static final String SOURCE_AUTH_LEVEL = "Source Authorization Level";
	public static final String TARGET_GROUP_ID= "Target Group Id";
}
