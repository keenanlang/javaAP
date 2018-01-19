package javaAP.yawt.img;

import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;
import java.awt.geom.AffineTransform;

import javaAP.lang.Obj;

/**
 * A Class for Image transformation functions.
 */

public class Transform extends Obj
{
    /**
     * Flips an image horizontally
     * 
     * @param input The image to flip
     * 
     * @return  A new BufferedImage containing
     *          the original image mirrored
     *          across the Y axis.
     */
    public static BufferedImage fliphorizontal(BufferedImage input)
    {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-input.getWidth(), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(input, null);
    }
    
    /**
     * Flips an image vertically
     * 
     * @param input The image to flip
     * 
     * @return  A new BufferedImage containing
     *          the original image mirrored
     *          across the Y axis.
     */
    public static BufferedImage flipvertical(BufferedImage input)
    {
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -input.getHeight());
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(input, null);
    }
    
    /**
     * Scales an image to a different size
     *
     * @param input The image to scale
     * @param scalex Horizontal scale factor
     * @param scaley Vertical scale factor
     * 
     */
    public static BufferedImage scale(BufferedImage input, double scalex, double scaley)
    {
	BufferedImage output = new BufferedImage((int) (scalex * input.getWidth()), (int) (scaley * input.getHeight()), BufferedImage.TYPE_INT_ARGB);
	
	AffineTransform tx = AffineTransform.getScaleInstance(scalex, scaley);
	AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
	op.filter(input, output);
	
	return output;
    }
}
