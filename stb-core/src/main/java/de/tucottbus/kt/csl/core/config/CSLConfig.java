package de.tucottbus.kt.csl.core.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.DC;

import de.tucottbus.kl.csl.Logg;

public class CSLConfig {
  private final static String CLASSKEY = "CSL configuration";

  private final static String FILEPATH = "CSLconfig.xml";

  public CSLConfig() {
    Logg.msg(CLASSKEY, "started");
  }

  @SuppressWarnings("unused")
  private File getResouce() {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource(FILEPATH).getFile());
    return file;
  }

  private void setModelToFile(Model model) {
    FileWriter out = null;
    try {
      out = new FileWriter(FILEPATH);
      model.write(out, "RDF/XML-ABBREV");
    } catch (IOException e) {
      Logg.err(CLASSKEY,
          "Could not find path for file. Error: " + e.getMessage());
    } finally {
      try {
        out.close();
        Logg.msg(CLASSKEY, "Config file successfully saved.");
      } catch (IOException e) {
        Logg.err(CLASSKEY,
            "Could not write config file. Error: " + e.getMessage());
      }
    }
  }

  private Model getModelFromFile() {
    Model model = ModelFactory.createDefaultModel();

    InputStream in = FileManager.get().open(FILEPATH);
    if (in == null) {
      throw new IllegalArgumentException("File: " + FILEPATH + " not found");
    }

    return model.read(in, null);
  }

  public void setConfig() {
    @SuppressWarnings("unused")
    String packagePath = "de.tucottbus.kt.speechlab.hardware.led";
    String name = "Led TV";
    String date = LocalDateTime.now().toString();

    Model model = ModelFactory.createDefaultModel();
    Resource res1 = model.createResource();
    res1.addProperty(DC.date, date);
    res1.addProperty(DC.creator, name);

    setModelToFile(model);
  }

  public void getConfig() {
    StmtIterator iter = getModelFromFile().listStatements();

    // print out the predicate, subject and object of each statement
    while (iter.hasNext()) {
      Statement stmt = iter.nextStatement(); // get next statement
      Resource subject = stmt.getSubject(); // get the subject
      Property predicate = stmt.getPredicate(); // get the predicate
      RDFNode object = stmt.getObject(); // get the object

      Logg.msg(CLASSKEY, "Subject: " + subject.toString());
      Logg.msg(CLASSKEY, "Predicate: " + predicate.toString());
      if (object instanceof Resource) {
        Logg.msg(CLASSKEY, "Object: " + object.toString());
      } else {
        // object is a literal
        Logg.msg(CLASSKEY, " \"" + object.toString() + "\"");
      }
    }
  }

  public static void main(String[] args) {
    CSLConfig config = new CSLConfig();
    config.setConfig();
    config.getConfig();
  }

}
