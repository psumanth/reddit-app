package org.baeldung.service.impl;

import java.util.TimeZone;

import org.baeldung.persistence.dao.PreferenceRepository;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.Preference;
import org.baeldung.persistence.model.User;
import org.baeldung.service.ISetupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SetupService implements ISetupService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void setupUser(final User user) {
        try {
            setupUserInternal(user);
        } catch (final Exception e) {
            logger.error("Error occurred while saving user " + user.toString(), e);
        }

    }

    //
    private void setupUserInternal(final User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPreference(createSimplePreference(user));
        userRepository.save(user);
    }

    private Preference createSimplePreference(final User user) {
        final Preference pref = new Preference();
        pref.setId(user.getId());
        pref.setTimezone(TimeZone.getDefault().getID());
        pref.setEmail(user.getUsername() + "@test.com");
        return preferenceRepository.save(pref);
    }

}
