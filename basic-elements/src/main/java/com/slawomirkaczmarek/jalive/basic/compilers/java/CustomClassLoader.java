package com.slawomirkaczmarek.jalive.basic.compilers.java;

import java.util.Map;

public class CustomClassLoader extends ClassLoader {
	private Map<String, CompiledCode> compiledClasses;

	public CustomClassLoader(ClassLoader classLoader) {
		super(classLoader);
	}
	
	public void addClass(CompiledCode compiledCode) {
		compiledClasses.put(compiledCode.getName(), compiledCode);
	}
	
	@Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
		Class<?> result = null;
        CompiledCode compiledCode = compiledClasses.get(name);
        if (compiledCode != null) {
        	byte[] code = compiledCode.getCode();
            result = defineClass(name, code, 0, code.length);
        } else {
        	result = super.findClass(name);
        }
        return result;
    }
}
