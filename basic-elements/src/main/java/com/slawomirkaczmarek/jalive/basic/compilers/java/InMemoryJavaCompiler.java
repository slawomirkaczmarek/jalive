package com.slawomirkaczmarek.jalive.basic.compilers.java;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

public class InMemoryJavaCompiler {

	static JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
	
	public static Class<?> compile(String className, String source) throws URISyntaxException, ClassNotFoundException {
		Class<?> result = null;
		SourceCode sourceCode = new SourceCode(className, source);
		CompiledCode compiledCode = new CompiledCode(className);
		Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(sourceCode);
		CustomClassLoader classLoader = new CustomClassLoader(ClassLoader.getSystemClassLoader());
		InMemoryJavaFileManager fileManager = new InMemoryJavaFileManager(
				javaCompiler.getStandardFileManager(null, null, null), 
				compiledCode, classLoader);
		List<String> options = new ArrayList<>();
		options.add( "-classpath");
		options.add(System.getProperty("java.class.path"));
		boolean status = javaCompiler.getTask(null, fileManager, null, options, null, compilationUnits).call();
		if (status) {
			result = classLoader.loadClass(className);
		}
		return result;
	}
}
