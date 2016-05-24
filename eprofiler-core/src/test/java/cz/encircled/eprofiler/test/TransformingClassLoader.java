package cz.encircled.eprofiler.test;

import java.lang.instrument.IllegalClassFormatException;

import cz.encircled.eprofiler.asm.AsmClassTransformer;

/**
 * @author Vlad on 23-May-16.
 */
public class TransformingClassLoader extends ClassLoader {

    private final String className;

    public TransformingClassLoader(String className) {
        super();
        this.className = className;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (name.equals(className)) {
            byte[] byteBuffer = new byte[0];
            try {
                byteBuffer = new AsmClassTransformer().transform(this, className, null, null, null);
            } catch (IllegalClassFormatException e) {
                e.printStackTrace();
            }
            return defineClass(className, byteBuffer, 0, byteBuffer.length);
        }
        return super.loadClass(name);
    }
}
