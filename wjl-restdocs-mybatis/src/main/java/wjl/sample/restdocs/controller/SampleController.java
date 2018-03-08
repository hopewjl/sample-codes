package wjl.sample.restdocs.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import wjl.sample.restdocs.App;
import wjl.sample.restdocs.dao.AppMapper;

/**
 * Created by wangjl on 18/3/8.
 */
@Controller
@RequestMapping("/wjl/sample/v1/restdocs")
public class SampleController {

    @Autowired
    AppMapper appMapper;

    @RequestMapping(value = "/insert/{appName}", method = RequestMethod.POST)
    public @ResponseBody
    JsonNode createApply(@PathVariable String appName, @RequestBody JsonNode request) throws Exception {
        try {
            App app = new App();
            app.setCreator(request.get("creator").asText());
            app.setDescribe(request.get("describe").asText());
            app.setName(appName);
            appMapper.insert(app);
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode jsonNode = objectMapper.createObjectNode();
            jsonNode.put("code", 0);
            jsonNode.put("msg", "OK");
            return jsonNode;
        } catch (Exception e) {

        }
        return null;
    }
}
