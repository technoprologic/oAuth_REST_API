package pl.ais.zychu;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.WebApplicationContext;
import pl.ais.zychu.controller.AlbumController;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = SpringSecurityRestServiceApplication.class)
public class AlbumControllerTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @InjectMocks
    AlbumController controller;

    private MockMvc mvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(springSecurityFilterChain).build();
    }

    @Test
    public void unauthorizedSampleRequestsTest() throws Exception {
        // @formatter:off
        mvc.perform(get("/api/albums")
                .accept(MediaType.ALL))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("unauthorized")));
        // @formatter:on

        // @formatter:off
        mvc.perform(get("/status/albums")
                .accept(MediaType.ALL))
                .andExpect(status().isOk());
        // @formatter:on
    }


    private String getAccessToken(String username, String password) throws Exception {
        String authorization = "Basic "
                + new String(Base64Utils.encode("clientapp:123456".getBytes()));
        String contentType = MediaType.APPLICATION_JSON.toString();

        // @formatter:off
        String content = mvc
                .perform(
                        post("/oauth/token")
                                .characterEncoding("UTF-8")
                                .header("Authorization", authorization)
                                .accept(contentType)
                                .contentType(
                                        MediaType.APPLICATION_FORM_URLENCODED)
                                .param("username", username)
                                .param("password", password)
                                .param("grant_type", "password")
                                .param("scope", "read write")
                                .param("client_id", "clientapp")
                                .param("client_secret", "123456"))
                .andExpect(status().isOk()) //400 ?
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.access_token", is(notNullValue())))
                .andExpect(jsonPath("$.token_type", is(equalTo("bearer"))))
                .andExpect(jsonPath("$.refresh_token", is(notNullValue())))
                .andExpect(jsonPath("$.expires_in", is(greaterThan(4000))))
                .andExpect(jsonPath("$.scope", is(equalTo("read write"))))
                .andReturn().getResponse().getContentAsString();

        // @formatter:on

        return content.substring(17, 53);
    }

    /** Authorization test
     *
     * curl -X POST -k -vu clientapp:123456 https://localhost:8443/oauth/token -H "Accept: application/json"
     * -d "password=spring&username=admin&grant_type=password&scope=read%20write&client_secret=123456&client_id=clientapp"
     *
     * @throws Exception
     */
    @Test
    public void authorizedRequests() throws Exception {
       // String accessToken = getAccessToken("admin", "spring");
        //...

    }


}
