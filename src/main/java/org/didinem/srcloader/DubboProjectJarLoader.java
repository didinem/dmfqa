package org.didinem.srcloader;

import java.io.IOException;
import java.util.jar.JarFile;

/**
 * Created by didinem on 3/4/2017.
 */
public class DubboProjectJarLoader {

    public static JarFile loadJar(String jarPath) throws IOException {
        return new JarFile(jarPath);
    }

}
