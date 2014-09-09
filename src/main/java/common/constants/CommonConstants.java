/**
 * 
 */
package common.constants;


/**
 * @author amLaptop
 *
 */
public class CommonConstants 
{
	public static class Error
	{
		// signup
		public static final int SUCCESS = 0;
		public static final int DUPLICATE_EMAIL_USERNAME = 1;
		public static final int DUPLICATE_EMAIL = 2;
		public static final int DUPLICATE_USERNAME = 3;
		public static final int DB_SAVE_ERROR = 4;
		public static final int ILLEGAL_VALUES = 5;
		
	}
	public static final int START_EMAIL_ACTION_DEF_ID = 1;
	
	private static int keyCount = 1;
	public static class Key
	{
		public static final Integer USER_BO = new Integer(keyCount++);
		public static final Integer DELETE_SUCCESS = new Integer(keyCount++);
		public static final Integer DELETE_FAIL = new Integer(keyCount++);
		
		public static final Integer VIEW_SUCCESS = new Integer(keyCount++);
		public static final Integer VIEW_FAIL = new Integer(keyCount++);
		public static final Integer REMARK_BO = new Integer(keyCount++);
		public static final Integer VIEW_MESSAGES = new Integer(keyCount++);
		public static final Integer LABEL_BO = new Integer(keyCount++);
		public static final Integer SHARED_RESOURCE_BO = new Integer(keyCount++);
	}
	
	
	// for URLEncoder.encode(xyz, DomainParams.ENCODING_UTF8);
	// see http://java.sun.com/javase/6/docs/api/java/nio/charset/Charset.html
	public static final String ENCODING_UTF8 = "UTF-8";
	
	public static final String COLON_SEP = ":";
	public static final String COMMA_SEP = ",";
	
	public static final String REFERER = "Referer";
    public static final String USER_AGENT = "User-Agent";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    
    public static final String GET_INSTANCE_METHOD = "getInstance";
	
	
	public static class Email
	{
		public static final int MAX_NUM_ATTEMPTS = 5;
	}
	
	public static final int MAX_DAYS_SIGN_UP_VALIDATION = 30; // validation key expires after 30 days
	public static final int MAX_DAYS_PASSWORD_VALIDATION = 1; // validation key expires after 1 day
	
	public static class Param
	{
		// init parameter name in web.xml 
		public static final String WEB_CONFIG_XML="webConfigXml";
	}
	
	// default values in case they are missing
	public static class Default
	{
		public static final Long DEFAULT_USER_ID = new Long(1);
	}
	
	// for html
	public static final String SHARE_RESPONSE_DIV = "shareResponseDiv";
	public static final String TOGGLE_PUBLIC_RESPONSE_DIV = "togglePublicResponseDiv";
	public static final String HIDDEN_OBJ_CSS_CLASS = "hiddenObj"; // defined in commonSkin/page.css

	/*
	 * during signup, an email is sent to the user.
	 * should an additional code be included in that email
	 * for the user to type in?
	 * 
	 * having a code adds security. however, it's cumersome for the
	 * user, and adds friction in the user-adoption process.
	 */
	public static final boolean USE_SIGN_UP_CODE_1 = false;
	
	/**
	 * if true, use doctype == html
	 */
	public static boolean FORCE_DOCTYPE_XHTML = true;
}
