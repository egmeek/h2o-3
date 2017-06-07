package ai.h2o.automl;

import org.junit.BeforeClass;
import org.junit.Test;
import water.Key;
import water.fvec.Frame;

import java.util.Date;

public class AutoMLTest extends TestUtil {

  @BeforeClass public static void setup() { stall_till_cloudsize(1); }

  @Test public void airlinesTest() {
    runAutoML("./smalldata/airlines/allyears2k_headers.zip","IsDepDelayed",20);
  }

  @Test public void weatherTest() {
    runAutoML("./smalldata/junit/weather.csv","RainTomorrow",20);
  }

  @Test public void carsTest() {
    runAutoML("./smalldata/junit/cars.csv","cylinders",20);
  }

  @Test public void irisTest() {runAutoML("./smalldata/iris/iris.csv","C5",20);}

  @Test public void prostateTest() {
    runAutoML("./smalldata/prostate/prostate.csv","AGE",20);
  }

  public Leaderboard runAutoML(String fname, String response, int runTime){
    AutoML aml=null;
    Frame fr=null;
    try {
      AutoMLBuildSpec autoMLBuildSpec = new AutoMLBuildSpec();
      fr = parse_test_file(fname);
      autoMLBuildSpec.input_spec.training_frame = fr._key;
      autoMLBuildSpec.input_spec.response_column = response;
      autoMLBuildSpec.build_control.loss = "AUTO";
      autoMLBuildSpec.build_control.stopping_criteria.set_max_runtime_secs(runTime);

      aml = AutoML.makeAutoML(Key.<AutoML>make(), new Date(), autoMLBuildSpec);
      AutoML.startAutoML(aml);
      aml.get();
      return aml.leaderboard();
    } finally {
      // cleanup
      if(aml!=null) aml.deleteWithChildren();
      if(fr != null) fr.remove();
    }
  }

}
