package com.slawomirkaczmarek.jalive.basic.compilers.java;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.tools.SimpleJavaFileObject;

public class CompiledCode extends SimpleJavaFileObject {
	private ByteArrayOutputStream outputStream;
	
	public CompiledCode(String className) throws URISyntaxException {
		super(new URI(className), Kind.CLASS);
		outputStream = new ByteArrayOutputStream();
	}
	
	@Override
	public OutputStream openOutputStream() throws IOException {
		return outputStream;
	}
	
	public byte[] getCode() {
		return outputStream.toByteArray();
	}
}
