package de.sizaz.stb.opencl.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.imageio.ImageIO;

import com.nativelibs4java.opencl.CLContext;
import com.nativelibs4java.opencl.CLDevice;
import com.nativelibs4java.opencl.CLEvent;
import com.nativelibs4java.opencl.CLImage2D;
import com.nativelibs4java.opencl.CLKernel;
import com.nativelibs4java.opencl.CLMem.Usage;
import com.nativelibs4java.opencl.CLSampler;
import com.nativelibs4java.opencl.CLSampler.AddressingMode;
import com.nativelibs4java.opencl.CLSampler.FilterMode;
import com.nativelibs4java.opencl.CLProgram;
import com.nativelibs4java.opencl.CLQueue;
import com.nativelibs4java.opencl.JavaCL;

public class CopyImageExample {
    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException {
        CLContext context = JavaCL.createBestContext();
        CLQueue queue = context.createDefaultQueue();

        CLDevice[] devices = context.getDevices();

        System.out.println("Devices count " + context.getDeviceCount());
        System.out.println("Max Units: "+devices[0].getMaxComputeUnits());
        System.out.println("CL Version: "+devices[0].getOpenCLVersion());

        /*
        ByteOrder byteOrder = context.getByteOrder();
        
        int n = 1024;
        Pointer<Float> aPtr = allocateFloats(n).order(byteOrder);
        Pointer<Float> bPtr = allocateFloats(n).order(byteOrder);
        Pointer<Float> oPtr = allocateFloats(n).order(byteOrder);

        for (int i = 0; i < n; i++) {
            aPtr.set(i, (float) cos(i));
            bPtr.set(i, (float) sin(i));
        }*/

        // Create OpenCL input buffers (using the native memory pointers aPtr
        // and bPtr) :

        String resourceName = "your_image.png";
        BufferedImage img = ImageIO.read(getResourcesStream(resourceName));

        CLImage2D inputImage = context.createImage2D(Usage.Input, img, false);

        // Create an OpenCL output buffer :
        CLImage2D outImage = context.createImage2D(Usage.Output, img, false);
        CLSampler sampler = context.createSampler(false, AddressingMode.ClampToEdge, FilterMode.Nearest);

        // Read the program sources and compile them :
        String src = convertStreamToString(getResourcesStream("CopyImageExample.cl")); 
        CLProgram program = context.createProgram(devices, src);

        // Get and call the kernel :
        CLKernel nnKernel = program.createKernel("rotate_image");
        
        nnKernel.setArgs(inputImage, outImage, sampler);
        CLEvent addEvt = nnKernel.enqueueNDRange(queue, new int[] { img.getWidth() , img.getHeight() });

        // blocks until rotate_image finished
        BufferedImage outPtr = outImage.read(queue, addEvt); 

        ImageIO.write(outPtr, "png", new File("../../../out.png"));
    }
    
    private static InputStream getResourcesStream(String resourceName){
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      return classLoader.getResourceAsStream(resourceName);
    }
    
    static String convertStreamToString(InputStream is) {
      Scanner s = new Scanner(is).useDelimiter("\\A");
      return s.hasNext() ? s.next() : "";
  }
}
