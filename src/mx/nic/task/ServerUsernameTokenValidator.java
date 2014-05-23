package mx.nic.task;

import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.handler.RequestData;
import org.apache.ws.security.message.token.UsernameToken;
import org.apache.ws.security.validate.Credential;
import org.apache.ws.security.validate.Validator;

/**
 * 
 * @author Jose
 *
 */
public class ServerUsernameTokenValidator implements Validator {

	@Override
	public Credential validate(Credential credential, RequestData data)
			throws WSSecurityException {
		UsernameToken usernameToken = credential.getUsernametoken();

		String username = usernameToken.getName();
		String password = usernameToken.getPassword();

		// hash the password

		if (!username.equals("jose") || !password.equals("pass")) {
			throw new WSSecurityException(WSSecurityException.FAILED_AUTHENTICATION);
		}

		return credential;
	}

}