import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nyu.rumors.RumorMillApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes=RumorMillApplication.class)
@AutoConfigureMockMvc
public class RumorMillTest {

    @Autowired
    private MockMvc mockMvc;

    private JSONParser parser = new JSONParser();

    @Test
    public void shouldReturnEmptyListOfRumors() throws Exception {
        Object jsonObject = parser.parse(this.mockMvc.perform(get("/api/rumor"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());

        JSONObject json = (JSONObject)jsonObject;

        assertThat(json.get("rumors").toString(), equalTo("[]"));
    }

    @Test
    public void shouldCreateNewRumor() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/api/rumor")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"brandon\", \"rumor\":\"listens to selena gomez\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        Object jsonObject = parser.parse(result.getResponse().getContentAsString());
        JSONObject json = (JSONObject)jsonObject;

        String id = json.get("id").toString();
        System.out.println("UUID: " + id);
    }

    @Test
    public void shouldCreateAndQueryRumorById() throws Exception {
        MvcResult postResult = this.mockMvc.perform(post("/api/rumor")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"brandon\", \"rumor\":\"has a jonas brothers poster\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        Object postJsonObject = parser.parse(postResult.getResponse().getContentAsString());
        JSONObject postJson = (JSONObject)postJsonObject;

        String id = postJson.get("id").toString();

        MvcResult getResult = this.mockMvc.perform(get("/api/rumor/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Object getJsonObject = parser.parse(getResult.getResponse().getContentAsString());
        JSONObject getJson = (JSONObject)getJsonObject;

        assertThat(postJson.get("id").toString(), equalTo(getJson.get("id").toString()));
        assertThat(postJson.get("name").toString(), equalTo(getJson.get("name").toString()));
        assertThat(postJson.get("rumor").toString(), equalTo(getJson.get("rumor").toString()));
    }

    @Test
    public void shouldFailToQueryNonexistentRumor() throws Exception {

        // here's a phony UUID
        String id = "00000000-0000-0000-0000-000000000000";

        this.mockMvc.perform(get("/api/rumor/" + id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateRumorsAndQueryListOfRumors() throws Exception {
        MvcResult firstPostResult = this.mockMvc.perform(post("/api/rumor")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"brandon\", \"rumor\":\"is a former vine celebrity\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        Object firstRumorJsonObject = parser.parse(firstPostResult.getResponse().getContentAsString());
        JSONObject firstRumorJson = (JSONObject)firstRumorJsonObject;

        MvcResult secondPostResult = this.mockMvc.perform(post("/api/rumor")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"alyssa\", \"rumor\":\"cheated on her spanish homework\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        Object secondRumorJsonObject = parser.parse(secondPostResult.getResponse().getContentAsString());
        JSONObject secondRumorJson = (JSONObject)secondRumorJsonObject;

        Object getRumorJsonObject = parser.parse(this.mockMvc.perform(get("/api/rumor"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());

        JSONObject getRumorJson = (JSONObject)getRumorJsonObject;

        assert getRumorJson.toJSONString().contains(firstRumorJson.get("id").toString());
        assert getRumorJson.toJSONString().contains(secondRumorJson.get("id").toString());
    }

    @Test
    public void shouldCreateDeleteAndQueryRumorById() throws Exception {
        MvcResult postResult = this.mockMvc.perform(post("/api/rumor")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"alyssa\", \"rumor\":\"doesn't even go to columbia\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        Object postJsonObject = parser.parse(postResult.getResponse().getContentAsString());
        JSONObject postJson = (JSONObject) postJsonObject;

        String id = postJson.get("id").toString();

        this.mockMvc.perform(delete("/api/rumor/" + id))
                .andDo(print())
                .andExpect(status().isNoContent());


        this.mockMvc.perform(get("/api/rumor/" + id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldNotDeleteNonexistentRumor() throws Exception {

        // here's a phony UUID
        String id = "00000000-0000-0000-0000-000000000000";

        this.mockMvc.perform(delete("/api/rumor/" + id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteAllRumors() throws Exception {

        this.mockMvc.perform(delete("/api/rumor"))
                .andDo(print())
                .andExpect(status().isNoContent());

        Object jsonObject = parser.parse(this.mockMvc.perform(get("/api/rumor"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());

        JSONObject json = (JSONObject)jsonObject;

        assertThat(json.get("rumors").toString(), equalTo("[]"));
    }

}
