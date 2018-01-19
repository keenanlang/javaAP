package javaAP.util;

import javaAP.lang.Obj;

/**
 * Vector is an immutable data type representing
 * a three dimensional vector or point.
 */
public class Vector extends Obj
{
    public final double x,y,z;

    public double getmagnitude()            { return Math.sqrt(this.dot(this)); }
    public void setmagnitude(double amt)    { this.scale(amt / this.getmagnitude()); }
    
    public double __magnitude__()           { return Math.sqrt(this.dot(this)); }
    public void __magnitude__(double amt)   { setmagnitude(amt); }
    public void __magnitude__(int amt)      { setmagnitude(amt); }
    
    private Vector(double x_in, double y_in, double z_in)
    {
        this.x = x_in;
        this.y = y_in;
        this.z = z_in;
    }
    
    // ************
    // Constructors
    // ************
    /**
     * Constructs a new Vector with the given x and y values
     * and no z value.
     * 
     * @param x_in  The Vector's x value
     * @param y_in  The Vector's y value
     */
    public static Vector instance(double x_in, double y_in)
    {
        return new Vector(x_in, y_in, 0); 
    }
    
    /**
     * Constructs a new Vector with the given x, y, and z
     * values.
     * 
     * @param x_in  The Vector's x value
     * @param y_in  The Vector's y value
     * @param z_in  The Vector's z value
     */
    public static Vector instance(double x_in, double y_in, double z_in)
    {
        return new Vector(x_in, y_in, z_in);
    }
    
    /**
     * Constructs a new Vector by adding the current vector and
     * the given one.
     * 
     * @param offset_Vector Another Vector to add to the current
     * 
     * @return a new Vector whose x, y, and z values are the sums
     *         of the current vector and the offset Vector.
     */
    public final Vector offset(Vector offset_Vector)
    {
        return this.offset(offset_Vector.x, offset_Vector.y, offset_Vector.z);
    }
    
    /**
     * Constructs a new Vector by adding the given x and y values
     * to the current Vector.
     * 
     * @param x_in  The amount to offset the x value
     * @param y_in  The amount to offset the y value
     * 
     * @return a new Vector whose x and y values are the sums of
     *         the current vector and the given values.
     */
    public final Vector offset(double x_in, double y_in)
    {
        return this.offset(x_in, y_in, 0);
    }
    
    /**
     * Constructs a new Vector by adding the given x, y, and z
     * values to the current Vector.
     * 
     * @param x_in  The amount to offset the x value
     * @param y_in  The amount to offset the y value
     * @param z_in  The amount to offset the z value
     * 
     * @return a new Vector whose x, y, and z values are the sums
     *         of the current vector and the given values.
     */
    public final Vector offset(double x_in, double y_in, double z_in)
    {
        return new Vector(this.x + x_in, this.y + y_in, this.z + z_in);
    }
    
    /**
     * Constructs a new Vector from the combination of an
     * offset and a scale operation.
     * 
     * @param offset_Vector The vector to be used for the offset
     *                      operation.
     * @param scale The amount to scale the input Vector.
     * 
     * @return a new Vector equivalent to offset(offset_Vector.scale(scale))
     */
    public final Vector offset(Vector offset_Vector, double scale)
    {
        return this.offset(offset_Vector.scale(scale));
    }
    
    /**
     * Constructs a new Vector in the same direction as the
     * current Vector with a magnitude increased by the given
     * ratio.
     * 
     * @param amt   The ratio of between the current Vector's
     *              magnitude and the output's.
     *              
     * @return  a new Vector with the appropriate scaling of the
     *          magnitude.
     */
    public final Vector scale(double amt)
    {
        return new Vector(x * amt, y * amt, z * amt);
    }
    
    /**
     * Computes the dot product between the current Vector and
     * the given vector.
     * 
     * @param other_Vector  The other Vector
     * 
     * @return  The dot product
     */
    public final double dot(Vector other_Vector)
    {
        return this.dot(other_Vector.x, other_Vector.y, other_Vector.z);
    }
    
    /**
     * Computes the dot product between the current Vector and
     * a Vector with the given x and y values.
     * 
     * @param x_in  The other Vector's x value
     * @param y_in  The other Vector's y value
     * 
     * @return  The dot product
     */
    public final double dot(double x_in, double y_in)
    {
        return this.dot(x_in, y_in, 0);
    }
    
    /**
     * Computes the dot product between the current Vector and
     * a Vector with the given x, y, and z values.
     * 
     * @param x_in  The other Vector's x value
     * @param y_in  The other Vector's y value
     * @param z_in  The other Vector's z value
     * 
     * @return  The dot product
     */
    public final double dot(double x_in, double y_in, double z_in)  { return this.x * x_in + this.y * y_in + this.z * z_in; }
    
    /**
     * Computes the cross product between the current Vector
     * and a given Vector.
     * 
     * @param other_Vector  The other Vector
     * 
     * @return  The cross product, you can invert this
     *          result to obtain the other possible
     *          solution to the cross product.
     */
    public final Vector cross(Vector other_Vector)
    {
        return this.cross(other_Vector.x, other_Vector.y, other_Vector.z);
    }
    
    /**
     * Computes the cross product between the current Vector
     * and a Vector with the given x, y, and z values.
     * 
     * @param x_in  The other Vector's x value
     * @param y_in  The other Vector's y value
     * @param z_in  The other Vector's z value
     * 
     * @return  The cross product, you can invert this
     *          result to obtain the other possible
     *          solution to the cross product.
     */
    public final Vector cross(double x_in, double y_in, double z_in)
    {
        return new Vector(this.y * z_in - this.z * y_in, this.z * x_in - this.x * z_in, this.x * y_in - this.y * x_in);
    }
    
    /**
     * Rotates the current Vector around the z-axis the given
     * number of radians. Useful when dealing in only two
     * dimensions.
     * 
     * @param theta The number of radians to rotate
     * 
     * @return  A new Vector that represents the rotation of
     *          the current Vector.
     */
    public final Vector rotate(double theta)
    {
        return this.rotate(0, 0, 1, theta);
    }
    
    /**
     * Rotates the current Vector around a Vector with the 
     * given x, y, and z values the given number of radians.
     * 
     * @param x_in  The other Vector's x value
     * @param y_in  The other Vector's y value
     * @param z_in  The other Vector's z value
     * @param theta The number of radians to rotate
     * 
     * @return  A new Vector that represents the rotation of
     *          the current Vector.
     */
    public final Vector rotate(double vector_x, double vector_y, double vector_z, double theta)
    {
        return this.rotate(new Vector(vector_x, vector_y, vector_z), theta);
    }
    
    /**
     * Rotates the current Vector around a given Vector
     * the given number of radians.
     * 
     * @param vector  The other Vector
     * @param theta The number of radians to rotate
     * 
     * @return  A new Vector that represents the rotation of
     *          the current Vector.
     */
    public final Vector rotate(Vector vector, double theta)
    {
        Vector unitvector = vector.normalize();
        
        Vector a = this.scale(Math.cos(theta));
        Vector b = unitvector.cross(this).scale(Math.sin(theta));
        Vector c = unitvector.scale(unitvector.dot(this) * (1 - Math.cos(theta)));
        
        return a.offset(b).offset(c);
    }
    
    /**
     * Inverts the direction of the current Vector
     * 
     * @return  A new Vector that represents the inverse
     *          of the current Vector.
     */
    public final Vector invert()
    {
        return new Vector(-this.x, -this.y, -this.z);
    }
    
    /**
     * Checks whether the current Vector is equivalent to
     * a Vector with the given x and y values.
     * 
     * @param x_in  The other Vector's x value
     * @param y_in  The other Vector's y value
     * 
     * @return  true, if the current Vector equals <x_in, y_in, 0>
     *          false, otherwise.
     */
    public boolean equals(double x_in, double y_in)
    {
        return this.equals(x_in, y_in, 0);
    }
    
    /**
     * Checks whether the current Vector is equivalent to
     * a Vector with the given x, y, and z values.
     * 
     * @param x_in  The other Vector's x value
     * @param y_in  The other Vector's y value
     * @param z_in  The other Vector's z value
     * 
     * @return  true, if the current Vector equals 
     *          <x_in, y_in, z_in>
     *          false, otherwise.
     */
    public boolean equals(double x_in, double y_in, double z_in)
    {
        return (this.x == x_in && this.y == y_in && this.z == z_in);
    }
    
    /**
     * Checks whether the current Vector is equivalent to
     * the given Vector.
     * 
     * @param compare   A Vector to compare against the current
     * 
     * @return  true, if the two Vectors are equivalent.
     *          false, otherwise.
     */
    public boolean equals(Vector compare)
    {
        return this.equals(compare.x, compare.y, compare.z);
    }
    
    /**
     * Checks whether the current Vector is equivalent to
     * the given Object.
     * 
     * @param compare   An Object to compare against the current
     *                  Vector.
     *                  
     * @return  true, if the Object is a Vector and equivalent to
     *          the current Vector.
     *          false, otherwise.
     */
    public boolean equals(Object compare)
    {
        if(compare instanceof Vector)   { return this.equals((Vector) compare); }
        else                            { return false; }
    }
    
    /**
     * Checks whether the current Vector lies within a rectangle given by
     * the parameters.
     * 
     * @param x_in  x value of lower left corner
     * @param y_in  y value of lower left corner
     * @param width Width of the rectangle
     * @param height Height of the rectangle
     * 
     * @return  true, if the current Vector lies within the given rectangle
     *          false, otherwise
     */
    public final boolean within(double x_in, double y_in, double width, double height)
    {
        return (this.x >= x_in && 
                this.x <= x_in + width && 
                this.y >= y_in && 
                this.y <= y_in + height); 
    }
    
    /**
     * Checks whether the current Vector lies within a rectangle described by
     * two opposite corners.
     * 
     * @param lowerleft The Vector position of the lower left corner of
     *                  the rectangle.
     * @param upperright    The Vector position of the upper right corner
     *                      of the rectangle.
     *                      
     * @return  true, if the current Vector lies within the given rectangle
     *          false, otherwise 
     */
    public final boolean within(Vector lowerleft, Vector upperright)
    {
        return (this.x >= lowerleft.x && 
                this.x <= upperright.x && 
                this.y >= lowerleft.y && 
                this.y <= upperright.y); 
    }
    
    /**
     * Constructs a new Vector in the same direction as the current
     * Vector, but with a magnitude of 1.0.
     * 
     * @return  A new Vector equivalent to scaling the current Vector
     *          according to the inverse of its magnitude
     */
    public final Vector normalize()
    {
        return this.scale(1.0 / this.getmagnitude());
    }
    
    /**
     * Computes the angle between the current Vector and a given Vector
     * in terms of radians.
     * 
     * @param other_Vector  The other Vector.
     * 
     * @return The smallest amount of radians between the current Vector
     *         and the given Vector.
     */
    public final double angle(Vector other_Vector)
    {
        Vector tempa = this.normalize();
        Vector tempb = other_Vector.normalize();
        
        return Math.acos(tempa.dot(tempb));
    }
    
    /**
     * Returns the String representation of this Vector
     * 
     * @return  Returns a String containing the x, y, and
     *          z values of this Vector separated by commas
     *          and surrounded by parentheses.
     */
    public String toString()    { return "(" + x + ", " + y + ", " + z + ")"; }
    
    
    // ***********
    
    // Obj Support
    // ***********
    
    public static Vector __intarray__(int[] array)
    {
        int temp_x = 0;
        int temp_y = 0;
        int temp_z = 0;
        
        if(array.length > 0)    { temp_x = array[0]; }
        if(array.length > 1)    { temp_y = array[1]; }
        if(array.length > 2)    { temp_z = array[2]; }
        
        if(array.length > 3)    { return null; }
        else                    { return Vector.instance(temp_x, temp_y, temp_z); }
    }
    
    public static Vector __doublearray__(double[] array)
    {
        
        double temp_x = 0.0;
        double temp_y = 0.0;
        double temp_z = 0.0;
        
        if(array.length > 0)    { temp_x = array[0]; }
        if(array.length > 1)    { temp_y = array[1]; }
        if(array.length > 2)    { temp_z = array[2]; }
        
        if(array.length > 3)    { return null; }
        else                    { return Vector.instance(temp_x, temp_y, temp_z); }
    }
    
    
    /**
     * Casts this Vector to an int array, for use with
     * .as(int[].type)
     * 
     * @return An int array of length 3 containing the
     *         int representation of this Vector's x, y,
     *         and z values.
     */
    public int[] __intarray__()
    {
        return new int[]{(int)this.x, (int)this.y, (int)this.z};
    }
    
    /**
     * Casts this Vector to a double array, for use with
     * .as(double[].type)
     * 
     * @return A double array of length 3 containing
     *         this Vector's x, y, and z values.
     */
    public double[] __doublearray__()
    {
        return new double[] {this.x, this.y, this.z};
    }
}
