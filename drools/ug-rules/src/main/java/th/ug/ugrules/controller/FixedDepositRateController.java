package th.ug.ugrules.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.kie.api.definition.type.FactType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import th.ug.ugrules.engine.config.DroolsConfig;
import th.ug.ugrules.model.FDRequest;

@RestController
@Tag(name = "测试 kie")
public class FixedDepositRateController {
    @Autowired
    private  KieContainer kieContainer;
    @Autowired
    private  KieContainer kieDeclareContainer;

    @RequestMapping(value = "/getFDInterestRate", method = RequestMethod.GET, produces = "application/json")
    public FDRequest getQuestions(@RequestParam(required = true) String bank, @RequestParam(required = true) Integer durationInYear) {
        KieSession kieSession = kieContainer.newKieSession();
        FDRequest fdRequest = new FDRequest(bank,durationInYear);
        kieSession.insert(fdRequest);
        kieSession.fireAllRules();
        kieSession.dispose();
        return fdRequest;
    }
    @RequestMapping(value = "/declare/test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getQuestions(@RequestParam(required = true) String name) {
        try{
            KieSession kieSession = kieDeclareContainer.newKieSession();
            // get the declared FactType
            FactType personType = kieDeclareContainer.getKieBase().getFactType("th.ug.rule","Person");
            Object bob = personType.newInstance();
            personType.set( bob, "name",  name);

            kieSession.insert(bob);
            kieSession.fireAllRules();
            kieSession.dispose();
            ObjectMapper mapper = new ObjectMapper();
            String res = mapper.writeValueAsString(bob);
            ObjectMapper mapper1 = new ObjectMapper();
            Object o2= mapper1.readValue(res,bob.getClass());

            return res ;
        }catch (Exception e){
            return "{\"msg\":"+"\"" + e.toString() +"\"}";
        }

    }
}
