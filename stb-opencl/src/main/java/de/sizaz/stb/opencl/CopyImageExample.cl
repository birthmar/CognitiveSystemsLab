__kernel void rotate_image(__read_only image2d_t input, __write_only image2d_t output, sampler_t sampler) {
    // Store each work-items unique row and column
    
    int2 coords = (int2){get_global_id(0), get_global_id(1)};
   	float4 pixel = read_imagef(input, sampler, coords);
    
    write_imagef(output, (int2){coords.y, coords.x}, pixel );
}