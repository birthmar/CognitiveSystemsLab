package de.tucottbus.kl.csl.config;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.VCARD;

import de.tucottbus.kl.csl.Logg;

public class CSLConfig {
  private static String CLASSKEY = "CSL configuration";
  
  public CSLConfig(){
    Logg.msg(CLASSKEY, "started");
  }
  
  public void setConfig(){
    
    
 // some definitions
    String personURI    = "http://somewhere/JohnSmith";
    String givenName    = "John";
    String familyName   = "Smith";
    String fullName     = givenName + " " + familyName;

    // create an empty Model
    Model model = ModelFactory.createDefaultModel();

    // create the resource
//       and add the properties cascading style
    Resource johnSmith
      = model.createResource(personURI)
             .addProperty(VCARD.FN, fullName)
             .addProperty(VCARD.N,
                          model.createResource()
                               .addProperty(VCARD.Given, givenName)
                               .addProperty(VCARD.Family, familyName));
  }
  
  public void getConfig(){
    
  }
  
  public static void main(String[] args){
    new CSLConfig().setConfig();
  }
  
}
