package org.keyanalysis.Services.DB;

import org.keyanalysis.Model.User;
import org.keyanalysis.Services.DigestService;

public final class LoginService {
	
	/**
	 * Bejelentkezést/Azonosítást végzõ metódus.
	 * @return Visszatér boolean értékkel, hogy sikerült-e azonosítani az usert és az user változóba teszi a megtalált Usert
	 * @author Bereczki Tamás
	 */
	public static boolean signIn(final String username, final String password, User user) {
		boolean succeeded = false;
		User u = new User();
		if(UserService.findAUser(username, u)) {
			if (u.getPassword().equals(LoginService.hashing(password))) {
				User user1 = u;
				if (user1.isDeleted())
					return false;
				user.setPassword(user1.getPassword());
				user.setName(user1.getName());
				user.setDeleted(user1.isDeleted());
				user.setStorage(user1.getStorage());
				succeeded = true;
			}
		}
		/*if(succeeded)
			LogService.AddLogEntry(user.getUserName() + ServiceConstants.LOGGEDIN, user, LoginService.class);
		*/
		return succeeded;
	}
	
	public static String hashing(final String pass) {
		return DigestService.getMD5Hash(pass);
	}
}
