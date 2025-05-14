package com.javarush.jira.bugtracking.sprint;

import com.javarush.jira.bugtracking.sprint.to.SprintTo;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.javarush.jira.bugtracking.sprint.SprintTestData.*;
import static com.javarush.jira.common.BaseHandler.REST_URL;
import static com.javarush.jira.common.util.JsonUtil.writeValue;
import static com.javarush.jira.login.internal.web.UserTestData.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SprintControllerTestDuplicate extends SprintControllerTest {
    private static final String MNGR_SPRINTS_REST_URL = REST_URL + "/mngr/sprints";

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicateCode() throws Exception {
        SprintTo duplicateCodeTo = new SprintTo(null, sprintTo1.getCode(), ACTIVE, PROJECT1_ID);
        perform(MockMvcRequestBuilders.post(MNGR_SPRINTS_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(duplicateCodeTo)))
                .andDo(print())
                .andExpect(status().isConflict());
    }


}
