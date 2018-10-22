package test.rest;

public class MediaAPIPathDecl {
	/*	APP MANAGER	*/
	public static final String AP_getMediaInfo = "/api/media/{mediaNo}";
	
	public static final String AP_getCodesTables = "/api/codes/tables";
	public static final String AP_getTreeCodes= "/api/codes/tree";
	public static final String AP_getCodeInTreeCodesByValue= "/api/codes/tree/values/{value}";
	
	public static final String AP_getMediaList = "/api/media";
	public static final String AP_getMediaContentsList = "/api/media/list";
	public static final String AP_GW_getMediaInfo= "/api/v{version}/media/{mediaNo}";
	public static final String AP_getPlan = "/api/v{version}/plans/published/live";
	

	
	
}
