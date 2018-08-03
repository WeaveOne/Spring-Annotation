package cn.willvi.typefilter;

import java.io.IOException;

import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

public class CustomTypeFilter implements TypeFilter {
	/**
	 * metadataReader the metadata reader for the target class 读取当前扫描类的信息
	 * metadataReaderFactory a factory for obtaining metadata readers
	 * for other classes (such as superclasses and interfaces) 获取其他类的信息
	 */
	public boolean match(MetadataReader reader, MetadataReaderFactory factory) throws IOException {
		ClassMetadata classMetadata = reader.getClassMetadata();
		String className = classMetadata.getClassName();
		System.out.println("----->"+className);
		if(className.contains("PersonService")) {
			return true;
		}
		return false;
	}

}
