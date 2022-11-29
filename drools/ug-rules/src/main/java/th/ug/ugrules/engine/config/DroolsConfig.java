package th.ug.ugrules.engine.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Date;

@Component
@Configuration
public class DroolsConfig {
    private static final String drlFile = "fdrules.drl";
    private ConcurrentHashMap<String,KieContainer> kieContainers = new ConcurrentHashMap<>();
    private  static  final  String sDrl = String.join("\npackage th.ug.rule\n",
            "import java.util.Date;\n",
            "declare Address\n",
            "   number : int\n",
            "   streetName : String\n",
            "   city : String\n",
            "end\n",
            "declare Person\n",
            "    name : String\n",
            "    dateOfBirth : Date\n",
            "    address : Address\n",
            "end\n",
            "rule \"Using a rule unit with a declared type\"\n",
            "  when\n",
            "    $p : Person( name == \"Bob\" )\n",
            "  then   // Insert Mark, who is a customer of James.\n",
            "    Address address = new Address();\n",
            "    address.setCity(\"Shenzhen\");\n",
            "    address.setStreetName(\"Aiguo\");\n",
            "    $p.setAddress(address);\n",
            "end\n");
    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource(drlFile));
         KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        KieModule kieModule = kieBuilder.getKieModule();
        return kieServices.newKieContainer(kieModule.getReleaseId());
    }

    @Bean
    public KieContainer kieDeclareContainer() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource("th/ug/rule/declare.drl"));
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        KieModule kieModule = kieBuilder.getKieModule();
        return kieServices.newKieContainer(kieModule.getReleaseId());
    }

    public  synchronized  KieContainer getKieContainerFromDRLString(String name) {
        KieContainer kieContainer =  kieContainers.get(name);
        if (kieContainer!=null){
            return  kieContainer;
        }
        String drl = this.readFromInputStream("th/ug/rule/declare.drl");
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write( "src/main/resources/th/ug/rule/"+name+".drl",
                kieServices.getResources().newReaderResource( new StringReader(drl) ) );
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        KieModule kieModule = kieBuilder.getKieModule();
        kieContainer =  kieServices.newKieContainer(kieModule.getReleaseId());
        return  kieContainer;
    }
   private  String readFromInputStream( String resFineName ) {
        try{
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resFineName);
                StringBuilder resultStringBuilder = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
            return resultStringBuilder.toString();
        }catch (Exception e){
            return  "";
        }
    }
}
