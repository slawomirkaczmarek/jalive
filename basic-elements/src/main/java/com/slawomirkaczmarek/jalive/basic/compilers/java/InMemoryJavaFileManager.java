package com.slawomirkaczmarek.jalive.basic.compilers.java;

import java.io.IOException;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;

public class InMemoryJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

	private CompiledCode compiledCode;
    private CustomClassLoader classLoader;
    
    protected InMemoryJavaFileManager(JavaFileManager fileManager, CompiledCode compiledCode, CustomClassLoader classLoader) {
        super(fileManager);
        this.compiledCode = compiledCode;
        this.classLoader = classLoader;
        this.classLoader.addClass(compiledCode);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling) throws IOException {
        return compiledCode;
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return classLoader;
    }
}
