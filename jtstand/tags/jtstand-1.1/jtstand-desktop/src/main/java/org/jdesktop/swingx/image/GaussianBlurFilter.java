/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, GaussianBlurFilter.java is part of JTStand.
 *
 * JTStand is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JTStand is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JTStand.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jdesktop.swingx.image;

import java.awt.image.BufferedImage;

import org.jdesktop.swingx.graphics.GraphicsUtilities;

public class GaussianBlurFilter extends AbstractFilter {
    private final int radius;

    /**
     * <p>Creates a new blur filter with a default radius of 3.</p>
     */
    public GaussianBlurFilter() {
        this(3);
    }

    /**
     * <p>Creates a new blur filter with the specified radius. If the radius
     * is lower than 0, a radius of 0.1 will be used automatically.</p>
     *
     * @param radius the radius, in pixels, of the blur
     */
    public GaussianBlurFilter(int radius) {
        if (radius < 1) {
            radius = 1;
        }

        this.radius = radius;
    }

    /**
     * <p>Returns the radius used by this filter, in pixels.</p>
     *
     * @return the radius of the blur
     */
    public int getRadius() {
        return radius;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "SuspiciousNameCombination" })
    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();

        if (dst == null) {
            dst = createCompatibleDestImage(src, null);
        }

        int[] srcPixels = new int[width * height];
        int[] dstPixels = new int[width * height];

        float[] kernel = createGaussianKernel(radius);

        GraphicsUtilities.getPixels(src, 0, 0, width, height, srcPixels);
        // horizontal pass
        blur(srcPixels, dstPixels, width, height, kernel, radius);
        // vertical pass
        blur(dstPixels, srcPixels, height, width, kernel, radius);
        // the result is now stored in srcPixels due to the 2nd pass
        GraphicsUtilities.setPixels(dst, 0, 0, width, height, srcPixels);

        return dst;
    }

    /**
     * <p>Blurs the source pixels into the destination pixels. The force of
     * the blur is specified by the radius which must be greater than 0.</p>
     * <p>The source and destination pixels arrays are expected to be in the
     * INT_ARGB format.</p>
     * <p>After this method is executed, dstPixels contains a transposed and
     * filtered copy of srcPixels.</p>
     *
     * @param srcPixels the source pixels
     * @param dstPixels the destination pixels
     * @param width the width of the source picture
     * @param height the height of the source picture
     * @param kernel the kernel of the blur effect
     * @param radius the radius of the blur effect
     */
    static void blur(int[] srcPixels, int[] dstPixels,
                     int width, int height,
                     float[] kernel, int radius) {
        float a;
        float r;
        float g;
        float b;

        int ca;
        int cr;
        int cg;
        int cb;

        for (int y = 0; y < height; y++) {
            int index = y;
            int offset = y * width;

            for (int x = 0; x < width; x++) {
                a = r = g = b = 0.0f;

                for (int i = -radius; i <= radius; i++) {
                    int subOffset = x + i;
                    if (subOffset < 0 || subOffset >= width) {
                        subOffset = (x + width) % width;
                    }

                    int pixel = srcPixels[offset + subOffset];
                    float blurFactor = kernel[radius + i];

                    a += blurFactor * ((pixel >> 24) & 0xFF);
                    r += blurFactor * ((pixel >> 16) & 0xFF);
                    g += blurFactor * ((pixel >>  8) & 0xFF);
                    b += blurFactor * ((pixel      ) & 0xFF);
                }

                ca = (int) (a + 0.5f);
                cr = (int) (r + 0.5f);
                cg = (int) (g + 0.5f);
                cb = (int) (b + 0.5f);

                dstPixels[index] = ((ca > 255 ? 255 : ca) << 24) |
                                   ((cr > 255 ? 255 : cr) << 16) |
                                   ((cg > 255 ? 255 : cg) <<  8) |
                                    (cb > 255 ? 255 : cb);
                index += height;
            }
        }
    }

    static float[] createGaussianKernel(int radius) {
        if (radius < 1) {
            throw new IllegalArgumentException("Radius must be >= 1");
        }

        float[] data = new float[radius * 2 + 1];

        float sigma = radius / 3.0f;
        float twoSigmaSquare = 2.0f * sigma * sigma;
        float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
        float total = 0.0f;

        for (int i = -radius; i <= radius; i++) {
            float distance = i * i;
            int index = i + radius;
            data[index] = (float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot;
            total += data[index];
        }

        for (int i = 0; i < data.length; i++) {
            data[i] /= total;
        }

        return data;
    }
}
