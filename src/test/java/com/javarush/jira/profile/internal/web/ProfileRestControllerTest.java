package com.javarush.jira.profile.internal.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.bugtracking.project.Project;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.ProfileMapper;
import com.javarush.jira.profile.internal.ProfileRepository;
import com.javarush.jira.profile.internal.model.Profile;
import com.javarush.jira.project.internal.web.ProjectTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static com.javarush.jira.common.util.JsonUtil.writeValue;
import static com.javarush.jira.login.internal.web.UserTestData.*;
import static com.javarush.jira.profile.internal.web.ProfileTestData.*;
import static com.javarush.jira.project.internal.web.ProjectTestData.PROJECT_MATCHER;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class ProfileRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = ProfileRestController.REST_URL;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    protected ProfileMapper profileMapper;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getUserProfile() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL ))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(PROFILE_TO_MATCHER.contentJson(USER_PROFILE_TO));
    }

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    void getGuestProfile() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL ))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(PROFILE_TO_MATCHER.contentJson(GUEST_PROFILE_EMPTY_TO));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void putNewProfile() throws Exception {
        ProfileTo newProfileTo = ProfileTestData.getNewTo();
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newProfileTo)))
                .andExpect(status().isNoContent());
       Profile profile =  profileRepository.getOrCreate(USER_ID);
       PROFILE_MATCHER.assertMatch(profile, ProfileTestData.getNew(USER_ID));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateProfile() throws Exception {
        ProfileTo updatedProfileTo = ProfileTestData.getUpdatedTo();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updatedProfileTo)))
                .andExpect(status().isNoContent());
        Profile profile =  profileRepository.getOrCreate(USER_ID);
        PROFILE_MATCHER.assertMatch(profile, ProfileTestData.getUpdated(USER_ID));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void invalidProfile() throws Exception {
        ProfileTo profileTo = ProfileTestData.getInvalidTo();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(profileTo)))
                .andExpect(status().isUnprocessableEntity());
    }


    @Test
    @WithUserDetails(value = USER_MAIL)
    void unknownNotificationTo() throws Exception {
        ProfileTo profileTo = ProfileTestData.getWithUnknownNotificationTo();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(profileTo)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void unknownContactTo() throws Exception {
        ProfileTo profileTo = ProfileTestData.getWithUnknownContactTo();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(profileTo)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void contactHtmlUnsafeTo() throws Exception {
        ProfileTo profileTo = ProfileTestData.getWithContactHtmlUnsafeTo();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(profileTo)))
                .andExpect(status().isUnprocessableEntity());
    }

}