package de.sizaz.stb.opencl;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Random;

import org.bridj.Pointer;

import com.nativelibs4java.opencl.CLBuffer;
import com.nativelibs4java.opencl.CLBuildException;
import com.nativelibs4java.opencl.CLContext;
import com.nativelibs4java.opencl.CLEvent;
import com.nativelibs4java.opencl.CLKernel;
import com.nativelibs4java.opencl.CLProgram;
import com.nativelibs4java.opencl.CLMem.Usage;
import com.nativelibs4java.opencl.CLQueue;
import com.nativelibs4java.opencl.JavaCL;
import com.nativelibs4java.util.IOUtils;

import de.sizaz.stb.Logg;

public class JAVACLMultiplication {
 
  private static final String CLASSKEY = "JAVACLMultiplication";
  
  private static final String FILENAME = "Vectormultiplicacaion.cl";
  
  public JAVACLMultiplication(){
    CLContext context = JavaCL.createBestContext();
    CLQueue queue = context.createDefaultQueue();
    ByteOrder byteOrder = context.getByteOrder();
    
    int n = 14447777;
    Pointer<Float>
      aPtr = Pointer.allocateFloats(n).order(byteOrder),
      bPtr = Pointer.allocateFloats(n).order(byteOrder);
    
    for(int i = 0; i < n; i++) {
      aPtr.set(i, (float) new Random().nextInt());
      bPtr.set(i, (float) new Random().nextInt());
    }
    
    CLBuffer<Float> a = context.createFloatBuffer(Usage.Input,n);
    CLBuffer<Float> b = context.createFloatBuffer(Usage.Input,n);
    
    CLBuffer<Float> out = context.createFloatBuffer(Usage.Output,n);
    
    Logg.msg(CLASSKEY, "used device memory: "+
        (a.getByteCount()+b.getByteCount()+out.getByteCount())/10000000 +"MB");
    
    // read the program sources and compile them:
    String src = getResources();
    CLProgram program = context.createProgram(src);
    
    long time = System.nanoTime();
    CLKernel addFloatsKernel  = program.createKernel("add_floats");
    addFloatsKernel.setArgs(a, b, out, n);
    CLEvent eventsToWaitFor = addFloatsKernel.enqueueNDRange(queue, new int[] { n });
    
    // blocks until add_floats finished
    Pointer<Float> outPtr = out.read(queue, eventsToWaitFor); 
    
    time = System.nanoTime() - time;
    Logg.msg(CLASSKEY,"computation in GPU took: " +(time/1000000)+"ms");
    
    //print the first 10 output values
    for(int i=0; i<100 && i<n; i++)
      Logg.msg(CLASSKEY,"out["+i+"] = "+outPtr.get(i));
    
  }
  
  private String getResources(){
    String src = null;
    try {
      return src = IOUtils
          .readText(JAVACLMultiplication.class.getResource(FILENAME));
    } catch (IOException e) {
      Logg.err(CLASSKEY, "Cannot find resources" + e.getMessage());
      return src;
    }
  }
  
  public static void main(String[] args) throws IOException, CLBuildException {
    new JAVACLMultiplication();
  }

}
