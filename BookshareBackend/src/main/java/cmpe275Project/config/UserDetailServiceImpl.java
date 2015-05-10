package cmpe275Project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import cmpe275Project.Model.*;
import cmpe275Project.services.LoginService;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
    private LoginService service;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      Login  account = service.findByAccountName(email);
        if(account == null) {
            throw new UsernameNotFoundException("no user found with " + email);
        }
        return new AccountUserDetails(account);
    }
}
