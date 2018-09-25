package app.service.authorization.constants;

import app.service.authorization.util.ExternalProperties;

/**
 * @author AdarshSinghal
 *
 */
public class ConstantsURL {

	public static final String GRP_AUTH_LVL_URL = ExternalProperties.getURL("app.service.user.getGrpAuthLvl.url");
	public static final String LOG_URL = ExternalProperties.getURL("app.service.logging.persist.url");
	
}
