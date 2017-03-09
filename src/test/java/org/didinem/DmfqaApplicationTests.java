package org.didinem;

import com.google.common.collect.Lists;
import org.didinem.analyze.Finder;
import org.didinem.handle.CacheHandler;
import org.didinem.srcloader.DubboProjectJarLoader;
import org.didinem.visitor.DubboAnalyzeClassVisitor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DmfqaApplicationTests {

	@Autowired
	private CacheHandler cacheHandler;

	private ApplicationContext applicationContext;

	@Autowired
	private DubboAnalyzeClassVisitor dubboAnalyzeClassVisitor;

	@Autowired
	private Finder finder;

	private List<String> testMethodList;

	private List<String> jarPath;

	{
		testMethodList = Lists.newArrayList(
				"com/lvtu/dao/ship/IShipGoodsDao.class",
				"com/lvtu/dao/ship/impl/ShipGoodsDaoImpl.class",
				"com/lvtu/service/api/rop/service/ship/ClientShipProductServiceImpl.class"
		);
		jarPath = Lists.newArrayList(
				"D:\\client-service-0.0.1-SNAPSHOT.jar",
				"D:\\client-dao-0.0.1-SNAPSHOT.jar"
		);
	}


	@Test
	public void contextLoads() throws IOException {
		for (String path : jarPath) {
			JarFile jarFile = DubboProjectJarLoader.loadJar(path);
			Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
			while (jarEntryEnumeration.hasMoreElements()) {
				JarEntry jarEntry = jarEntryEnumeration.nextElement();
				String jarEntryName = jarEntry.getName();
				if (testMethodList.contains(jarEntryName)) {
					InputStream inputStream = new BufferedInputStream(jarFile.getInputStream(jarEntry), 1024);
					ClassReader classReader = new ClassReader(inputStream);
					classReader.accept(dubboAnalyzeClassVisitor, 0);
				}
			}
			jarFile.close();
		}
		System.out.println("");
	}

	@Test
	public void findDen() {
		String rootKey = "com/lvtu/service/api/rop/service/ship/ClientShipProductServiceImpl:getCategoryCruiseList:(Lcom/lvmama/vst/api/compship/prod/vo/CompShipProductVo;Ljava/util/Date;Z)Ljava/util/List;";
		finder.findDen(rootKey, "");
	}

}
