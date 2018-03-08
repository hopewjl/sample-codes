package wjl.sample.restdocs.controller;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.cli.CliDocumentation.curlRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppControllerTest {


    @Autowired
    private WebApplicationContext webApplicationContext;
    /**
     * 用来表明文档片段的生成位置
     */
    @Rule//类似于Before注解
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.webApplicationContext)
                .apply(documentationConfiguration(this.restDocumentation)
                        .snippets()
                        .withDefaults(httpRequest(), httpResponse(), curlRequest()).and())
                .build();
    }

    @Test
    public void testApplyApp() throws Exception {
        this.mockMvc.perform(post("/wjl/sample/v1/restdocs/insert/{appName}", "Test").contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\t\"creator\": \"wangjinglei\",\n" +
                        "\t\"describe\": \"spring restdocs+mybatis sample\"\n" +
                        "}").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("appApply",
                        pathParameters(parameterWithName("appName").description("App's Name")),
                        requestFields(

                                fieldWithPath("creator").description("the app creator"),
                                fieldWithPath("describe").description("description of the app")),

                        responseFields(
                                fieldWithPath("code").description("the return code ")
                                , fieldWithPath("msg").description("description corresponding the return code! "))));

    }

}
