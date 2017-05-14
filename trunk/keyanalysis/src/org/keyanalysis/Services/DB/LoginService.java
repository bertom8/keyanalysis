package org.keyanalysis.Services.DB;

import org.keyanalysis.Model.User;
import org.keyanalysis.Services.DigestService;

public final class LoginService {

	/**
	 * Bejelentkez�st/Azonos�t�st v�gz� met�dus.
	 * 
	 * @return Visszat�r boolean �rt�kkel, hogy siker�lt-e azonos�tani az usert
	 *         �s az user v�ltoz�ba teszi a megtal�lt Usert
	 * @author Bereczki Tam�s
	 */
	public static boolean signIn(final String username, final String password, final User user) {
		boolean succeeded = false;
		final User u = new User();
		if (UserService.findAUser(username, u)) {
			if (u.getPassword().equals(LoginService.hashing(password))) {
				final User user1 = u;
				if (user1.isDeleted()) {
					return false;
				}
				user.setPassword(user1.getPassword());
				user.setName(user1.getName());
				user.setDeleted(user1.isDeleted());
				user.setStorage(user1.getStorage());
				succeeded = true;
			}
		}
		/*
		 * if(succeeded) LogService.AddLogEntry(user.getUserName() +
		 * ServiceConstants.LOGGEDIN, user, LoginService.class);
		 */
		return succeeded;
	}

	public static String hashing(final String pass) {
		return DigestService.getMD5Hash(pass);
	}
}
